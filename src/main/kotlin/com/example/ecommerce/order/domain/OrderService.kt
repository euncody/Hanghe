package com.example.ecommerce.order.domain

import com.example.ecommerce.global.lock.RedisLock
import com.example.ecommerce.order.domain.event.OrderPlacedEvent
import com.example.ecommerce.product.domain.ProductService
import jakarta.transaction.Transactional
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val productService: ProductService,
    private val eventPublisher: ApplicationEventPublisher
) {
    fun getOrderById(orderId: String) = orderRepository.findByOrderId(orderId)

    @Transactional
    @RedisLock(key = "order:create:#{#order.userId}", waitTimeMs = 2000, leaseTimeMs = 10000)
    fun createOrder(order: Order): Order {
        val user = order.user
        if (user.point < order.totalAmount) {
            throw IllegalStateException("포인트 부족")
        }

        for (item in order.orderItems) {
            val productKey = item.productKey ?: throw IllegalArgumentException("상품 키 없음")
            val product = productService.getProductKey(productKey)

            if (product.amount < item.quantity) {
                throw IllegalStateException("상품 재고 부족")
            }

            val updatedProduct = product.decreaseStock(item.quantity)
            productService.updateProduct(updatedProduct)
        }

        val saved = orderRepository.save(order)

        eventPublisher.publishEvent(
            OrderPlacedEvent(
                orderId = saved.orderId,
                userId = saved.user.userId,
                totalAmount = saved.totalAmount,
                productSummaries = saved.orderItems.map {
                    OrderPlacedEvent.ProductSummary(
                        productKey = requireNotNull(it.productKey) { "상품 키 없음" },
                        quantity = it.quantity,
                        priceAtOrder = it.priceAtOrder
                    )
                }
            )
        )

        return saved
    }


    fun deleteOrder(orderId: String) = orderRepository.delete(orderId)

    fun getAllOrders() = orderRepository.findAll()

    fun getOrdersByUserId(userId: String) = orderRepository.findByUserId(userId)

    fun updateOrder(order: Order) = orderRepository.update(order)

    fun getOrdersByProductKey(productCode: String): List<Order> {
        return orderRepository.findByProductKey(productCode.toLong())
    }
}