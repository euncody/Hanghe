package com.example.ecommerce.order.infrastructure.persistence

import com.example.ecommerce.order.OrderMapper
import com.example.ecommerce.order.domain.Order
import com.example.ecommerce.order.domain.OrderRepository
import com.example.ecommerce.product.domain.ProductService
import com.example.ecommerce.user.domain.UserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl (
    private val jpaRepository: OrderJpaRepository,
    private val orderMapper: OrderMapper,
    private val userService: UserService,
    private val productService: ProductService
) : OrderRepository {

    override fun findByOrderId(orderId: String): Order? {
        val entity = jpaRepository.findByOrderId(orderId) ?: return null
        return orderMapper.toDomain(entity)
    }

    @Transactional
    override fun save(order: Order): Order {
        // 유저 확인
        val userId = order.user.userId
        val user = userService.getUserByUserId(userId)

        // 잔액 확인 및 차감
        val totalAmount = order.totalAmount
        if (user.point < totalAmount) {
            throw IllegalStateException("잔액이 부족합니다.")
        }
        userService.useUserPoint(userId, totalAmount)

        // 재고 차감
        for (item in order.orderItems) {
            val productKey = item.productKey
                ?: throw IllegalArgumentException("상품 키가 없습니다.")

            val product = productService.getProductKey(productKey)
            if (product.amount < item.quantity) {
                throw IllegalStateException("상품(${product.productName})의 재고가 부족합니다.")
            }

            val updatedProduct = product.decreaseStock(item.quantity)
            productService.updateProduct(updatedProduct)
        }

        // 주문 저장
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
