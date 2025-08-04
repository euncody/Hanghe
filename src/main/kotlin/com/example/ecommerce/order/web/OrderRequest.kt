package com.example.ecommerce.order.web

import com.example.ecommerce.order.domain.Order
import com.example.ecommerce.order.domain.OrderItem
import com.example.ecommerce.user.web.UserRequest

data class OrderRequest(
    val orderId: String = "",
    val user: UserRequest,
    val orderStatus: String,
    val totalAmount: Int,
    val orderItems: List<OrderItemRequest>
) {
    fun toDomain(): Order {
        return Order(
            orderId = orderId,
            user = user.toDomain(),
            orderStatus = Order.OrderStatus.valueOf(orderStatus),
            totalAmount = totalAmount,
            orderItems = orderItems.map { it.toDomain() }
        )
    }
}

data class OrderItemRequest(
    val orderItemId: String,
    val productKey: Long,
    val quantity: Int,
    val priceAtOrder: Int
) {
    fun toDomain(): OrderItem {
        return OrderItem(
            orderItemId = orderItemId,
            productKey = productKey,
            quantity = quantity,
            priceAtOrder = priceAtOrder
        )
    }
}
