package com.example.ecommerce.order.infrastructure.persistence

import com.example.ecommerce.order.infrastructure.entity.OrderItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface OrderItemRankingRepository : JpaRepository<OrderItemEntity, Long> {

    interface PopularProductProjection {
        val productKey: Long
        val productCode: String
        val productName: String
        val totalQty: Long
        val totalRev: Long
    }

    @Query(
        value = """
            SELECT 
            
                p.product_key  AS productKey,
                p.product_code AS productCode,
                p.product_name AS productName,
                SUM(oi.quantity)                     AS totalQty,
                SUM(oi.quantity * oi.price_at_order) AS totalRev
            FROM orders o
            JOIN order_item oi ON oi.order_key = o.order_key
            JOIN product p     ON p.product_key = oi.product_key
            WHERE o.order_status IN ('COMPLETED','DELIVERING')
                AND ( ?2 IS NULL OR o.order_date >= ?2 )
                AND ( ?3 IS NULL OR o.order_date <  DATE_ADD(?3, INTERVAL 1 DAY) )
            GROUP BY p.product_key, p.product_code, p.product_name
            ORDER BY totalQty DESC, totalRev DESC
            LIMIT ?1
        """,
        nativeQuery = true
    )

    fun findPopularProducts(
        limit: Int,             // ?1
        startDate: LocalDate?,  // ?2
        endDate: LocalDate?     // ?3
    ): List<PopularProductProjection>

}