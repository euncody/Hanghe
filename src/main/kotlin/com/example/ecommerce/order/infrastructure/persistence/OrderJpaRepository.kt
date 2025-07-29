package com.example.ecommerce.order.infrastructure.persistence

import com.example.ecommerce.order.infrastructure.entity.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderJpaRepository : JpaRepository<OrderEntity, Long> {
    fun findByOrderId(orderId: String): OrderEntity?
    fun findByUser_UserId(userId: String): List<OrderEntity>
}