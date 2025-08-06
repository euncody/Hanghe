package com.example.ecommerce.order.domain

interface OrderRepository {
    fun findByOrderId(orderId: String): Order?
    fun save(order: Order): Order
    fun delete(orderId: String)
    fun findAll(): List<Order>
    fun findByUserId(userId: String): List<Order>
    fun update(order: Order): Order
    fun findByProductKey(productKey: Long): List<Order>


    fun findWithLock(orderId: String): Order
}