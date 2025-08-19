package com.example.ecommerce.order.domain

import com.example.ecommerce.order.infrastructure.persistence.OrderItemRankingRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ProductRankingService(
    private val rankingRepo: OrderItemRankingRepository
) {
    fun findPopularProducts(
        topN: Int = 10,
        startDate: LocalDate? = null,
        endDate: LocalDate? = null
        ): List<PopularProductDto> {
        return rankingRepo.findPopularProducts(topN, startDate, endDate).map {
            PopularProductDto(
                productKey = it.productKey,
                productCode = it.productCode,
                productName = it.productName,
                totalQty = it.totalQty,
                totalRev = it.totalRev
            )
        }
    }
}

data class PopularProductDto(
    val productKey: Long,
    val productCode: String,
    val productName: String,
    val totalQty: Long,
    val totalRev: Long
)