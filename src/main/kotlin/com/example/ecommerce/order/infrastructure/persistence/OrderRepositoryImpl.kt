package com.example.ecommerce.order.infrastructure.persistence

import com.example.ecommerce.order.OrderMapper
import com.example.ecommerce.order.domain.Order
import com.example.ecommerce.order.domain.OrderRepository
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl (
    private val jpaRepository: OrderJpaRepository,
    private val orderMapper: OrderMapper
) : OrderRepository {

    override fun findByOrderId(orderId: String): Order? {
        TODO("Not yet implemented")
    }

    override fun save(order: Order): Order {
        TODO("Not yet implemented")
    }

    override fun delete(orderId: String) {
        TODO("Not yet implemented")
    }

    override fun findAll(): List<Order> {
        TODO("Not yet implemented")
    }

    override fun findByUserId(userId: String): List<Order> {
        TODO("Not yet implemented")
    }

    override fun update(order: Order): Order {
        TODO("Not yet implemented")
    }

    override fun findByProductCode(productCode: String): List<Order> {
        TODO("Not yet implemented")
    }

}
