package com.example.ecommerce.order.domain

import com.example.ecommerce.user.domain.User
import java.time.LocalDateTime

data class Order(
    val orderKey: Long = 0,
    val orderId: String,
    val user: User,
    val orderStatus: OrderStatus = OrderStatus.COMPLETED,
    val totalAmount: Int,
    val orderItems: List<OrderItem> = emptyList(),
    val orderDate: LocalDateTime = LocalDateTime.now()
) {

    enum class OrderStatus {
        COMPLETED,
        CANCELLED,
        DELIVERING
    }

    fun updateOrderStatus(newStatus: OrderStatus): Order {
        return this.copy(orderStatus = newStatus)
    }

    fun getOrderInfo(): String {
        return "주문 ID: $orderId, 사용자 ID: ${user.userKey}, 총 금액: $totalAmount, 상태: $orderStatus, 항목 수: ${orderItems.size}"
    }

    companion object {
        fun registerOrder(orderId: String, user: User, orderItems: List<OrderItem>): Order {
            require(orderId.isNotBlank()) { "주문 ID는 비어 있을 수 없습니다." }
            require(orderItems.isNotEmpty()) { "주문 항목은 1개 이상이어야 합니다." }

            val total = orderItems.sumOf { it.quantity * it.priceAtOrder } // 총 금액을 자동으로 계산

            return Order(
                orderId = orderId,
                user = user,
                totalAmount = total,
                orderItems = orderItems
            )
        }
    }
}
