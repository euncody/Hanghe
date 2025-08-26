package com.example.ecommerce.order.web

import com.example.ecommerce.order.domain.PopularProductResponse
import com.example.ecommerce.order.domain.ProductRankingService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@RestController
@RequestMapping("/api/rank")
class RankingController(
    private val rankingService: ProductRankingService
) {
    // yyyy-MM-dd 또는 yyyyMMdd 모두 허용
    private val flexibleDateFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("[yyyy-MM-dd][yyyyMMdd]")

    @Operation(
        summary = "인기상품 랭킹 조회(수량 기준, Redis 우선)",
        description = "상위 N개 인기상품을 반환합니다. 날짜가 없으면 전체 기간, 있으면 해당 기간(포함)으로 필터합니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "성공"),
            ApiResponse(responseCode = "400", description = "잘못된 요청(날짜 형식/범위 오류)")
        ]
    )
    @GetMapping("/popular")
    fun popular(
        @Parameter(
            description = "상위 N개 (기본 10)",
            example = "10"
        )
        @RequestParam(defaultValue = "10")
        topN: Int,

        @Parameter(
            description = "시작일 (허용: yyyy-MM-dd 또는 yyyyMMdd)",
            schema = Schema(type = "string", example = "2025-08-01")
        )
        @RequestParam(required = false)
        startDate: String?,

        @Parameter(
            description = "종료일 (허용: yyyy-MM-dd 또는 yyyyMMdd)",
            schema = Schema(type = "string", example = "20250819")
        )
        @RequestParam(required = false)
        endDate: String?
    ): List<PopularProductResponse> {
        val start: LocalDate? = parseDateOrNull(startDate)
        val end: LocalDate? = parseDateOrNull(endDate)

        if (start != null && end != null && start.isAfter(end)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "startDate must be <= endDate")
        }

        return rankingService.findPopularProducts(topN, start, end)
    }

    private fun parseDateOrNull(raw: String?): LocalDate? {
        if (raw.isNullOrBlank()) return null
        return try {
            LocalDate.parse(raw, flexibleDateFormatter)
        } catch (e: DateTimeParseException) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid date format: '$raw' (use yyyy-MM-dd or yyyyMMdd)"
            )
        }
    }
}
