package com.example.ecommerce.order.infrastructure.persistence

import com.example.ecommerce.order.infrastructure.entity.OrderItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

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
            GROUP BY p.product_key, p.product_code, p.product_name
            ORDER BY totalQty DESC, totalRev DESC
            LIMIT :limit
        """,
        nativeQuery = true
    )

    fun findPopularProducts(@Param("limit") limit: Int): List<PopularProductProjection>

}