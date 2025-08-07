package com.example.ecommerce.order.infrastructure.persistence

import com.example.ecommerce.order.infrastructure.entity.OrderEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface OrderJpaRepository : JpaRepository<OrderEntity, Long> {
    fun findByOrderId(orderId: String): OrderEntity?
    fun findByUser_UserId(userId: String): List<OrderEntity>
    fun findByOrderItems_ProductKey(productKey: Long): List<OrderEntity>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM OrderEntity o WHERE o.orderId = :orderId")
    fun findByOrderIdWithLock(orderId: String): OrderEntity?
}