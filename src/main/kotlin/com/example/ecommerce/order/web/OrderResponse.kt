package com.example.ecommerce.order.web

import com.example.ecommerce.order.domain.Order
import com.example.ecommerce.order.domain.OrderItem
import com.example.ecommerce.user.web.UserResponse

data class OrderResponse(
    val orderId: String,
    val user: UserResponse,
    val orderStatus: String,
    val totalAmount: Int,
    val orderItems: List<OrderItemResponse>
) {
    companion object {
        fun from(order: Order): OrderResponse {
            return OrderResponse(
                orderId = order.orderId,
                user = UserResponse.from(order.user),
                orderStatus = order.orderStatus.name,
                totalAmount = order.totalAmount,
                orderItems = order.orderItems.map { OrderItemResponse.from(it) }
            )
        }
    }
}

data class OrderItemResponse(
    val orderItemId: String,
    val productKey: Long,
    val quantity: Int,
    val priceAtOrder: Int
) {
    companion object {
        fun from(orderItem: OrderItem): OrderItemResponse {
            return OrderItemResponse(
                orderItemId = orderItem.orderItemId,
                productKey = orderItem.productKey ?: throw IllegalArgumentException("Product key cannot be null"),
                quantity = orderItem.quantity,
                priceAtOrder = orderItem.priceAtOrder
            )
        }
    }
}
