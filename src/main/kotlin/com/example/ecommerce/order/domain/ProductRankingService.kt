package com.example.ecommerce.order.domain

import com.example.ecommerce.order.infrastructure.persistence.OrderItemRankingRepository
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
 * 인기상품 랭킹 조회 Service (Redis 우선 + DB 폴백)
 *
 * - 날짜 미지정: ZSET "rank:product:qty:ALL" 에서 TOP N 조회
 * - 날짜 지정:   일자별 ZSET("rank:product:qty:yyyyMMdd")들을 ZUNIONSTORE로 합쳐 임시키에서 TOP N 조회
 * - 캐시 미스:   DB 네이티브 쿼리로 폴백 후(옵션) ALL 키 워밍
 *
 * 반환 타입은 기존 PopularProductDto 그대로 사용하여 외부 영향 최소화.
 */
@Service
class ProductRankingService(
    private val rankingRepo: OrderItemRankingRepository,
    private val redis: StringRedisTemplate
) {
    private val KEY_ALL = "rank:product:qty:ALL"
    private val KEY_DAILY_PREFIX = "rank:product:qty:"
    private val df: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

    fun findPopularProducts(
        topN: Int = 10,
        startDate: LocalDate? = null,
        endDate: LocalDate? = null
    ): List<PopularProductResponse> {
        // 1) Redis 우선
        val cached = getFromRedis(topN, startDate, endDate)
        if (cached.isNotEmpty()) return cached

        // 2) 캐시 미스 → DB 폴백
        val fromDb = rankingRepo.findPopularProducts(topN, startDate, endDate).map {
            PopularProductResponse(
                productKey = it.productKey,
                productCode = it.productCode,
                productName = it.productName,
                totalQty = it.totalQty,
                totalRev = it.totalRev
            )
        }

        // 3)ALL 요청일 때만 전체 키 워밍
        if (startDate == null && endDate == null && fromDb.isNotEmpty()) {
            warmAllKey(fromDb)
        }

        return fromDb
    }

    /**
     * Redis에서 TOP N 조회
     * - 상품 메타(productCode/name)는 여기선 비워둔다. 필요 시 HASH/DB로 보강 가능.
     */
    private fun getFromRedis(
        topN: Int,
        start: LocalDate?,
        end: LocalDate?
    ): List<PopularProductResponse> {
        val z = redis.opsForZSet()

        // 전체 기간 조회: ALL 키 사용
        if (start == null && end == null) {
            val tuples = z.reverseRangeWithScores(KEY_ALL, 0, (topN - 1).toLong()) ?: return emptyList()
            if (tuples.isEmpty()) return emptyList()
            return tuples.toDtoList()
        }

        // 기간 합산: 일자 키들을 ZUNIONSTORE로 합쳐 임시키에서 조회
        val startDate = start ?: LocalDate.of(1970, 1, 1)
        val endDate = end ?: LocalDate.now()
        if (startDate.isAfter(endDate)) return emptyList()

        val dailyKeys = buildDailyKeys(startDate, endDate)
        if (dailyKeys.isEmpty()) return emptyList()

        val destKey = "tmp:rank:product:qty:${UUID.randomUUID()}"
        try {
            val first = dailyKeys.first()
            val others = if (dailyKeys.size > 1) dailyKeys.drop(1) else emptyList()
            val unionCount = z.unionAndStore(first, others, destKey) ?: 0
            if (unionCount <= 0L) return emptyList()

            // 임시키에 짧은 TTL
            redis.expire(destKey, 30, TimeUnit.SECONDS)

            val tuples = z.reverseRangeWithScores(destKey, 0, (topN - 1).toLong()) ?: return emptyList()
            if (tuples.isEmpty()) return emptyList()
            return tuples.toDtoList()
        } finally {
            // 임시키 정리(원하면 TTL만 두고 삭제 생략 가능)
            redis.delete(destKey)
        }
    }

    private fun Set<ZSetOperations.TypedTuple<String>>.toDtoList(): List<PopularProductResponse> =
        this.map {
            PopularProductResponse(
                productKey = it.value.toString().toLong(),
                productCode = "",
                productName = "",
                totalQty = it.score?.toLong() ?: 0L,
                totalRev = 0L
            )
        }

    /**
     * [start, end] 일자 범위의 일별 키 리스트 생성
     * 안전장치로 최대 180일까지만 생성
     */
    private fun buildDailyKeys(start: LocalDate, end: LocalDate): List<String> {
        val keys = ArrayList<String>()
        var d = start
        var count = 0
        val maxDays = 180
        while (!d.isAfter(end)) {
            keys.add(KEY_DAILY_PREFIX + d.format(df))
            d = d.plusDays(1)
            count++
            if (count > maxDays) break
        }
        return keys
    }

    /** DB 폴백 결과로 ALL 키 워밍 */
    private fun warmAllKey(rows: List<PopularProductResponse>) {
        val z = redis.opsForZSet()
        redis.delete(KEY_ALL)
        rows.forEach { dto ->
            z.add(KEY_ALL, dto.productKey.toString(), dto.totalQty.toDouble())
        }
    }
}
