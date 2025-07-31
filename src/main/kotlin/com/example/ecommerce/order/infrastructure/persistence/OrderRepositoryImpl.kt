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
        val entity = jpaRepository.findByOrderId(orderId) ?: return null
        return orderMapper.toDomain(entity)
    }

    override fun save(order: Order): Order {
        val entity = orderMapper.toEntity(order)
        val savedEntity = jpaRepository.save(entity)
        return orderMapper.toDomain(savedEntity)
    }

    override fun delete(orderId: String) {
        val entity = jpaRepository.findByOrderId(orderId) ?: return
        jpaRepository.delete(entity)
    }

    override fun findAll(): List<Order> {
        val entities = jpaRepository.findAll()
        return entities.map { orderMapper.toDomain(it) }
    }

    override fun findByUserId(userId: String): List<Order> {
        val entities = jpaRepository.findByUser_UserId(userId)
        return entities.map { orderMapper.toDomain(it) }
    }

    override fun update(order: Order): Order {
        val existingEntity = jpaRepository.findByOrderId(order.orderId) ?: return order
        val updatedEntity = orderMapper.toEntity(order).apply {
            orderKey = existingEntity.orderKey // 유지할 키 값
        }
        val savedEntity = jpaRepository.save(updatedEntity)
        return orderMapper.toDomain(savedEntity)
    }

    override fun findByProductKey(productKey: Long): List<Order> {
        val entities = jpaRepository.findByOrderItems_ProductKey(productKey)
        return entities.map { orderMapper.toDomain(it) }
    }


}
