package com.example.ecommerce.domain.order.service

import com.example.ecommerce.domain.order.model.request.OrderRequest
import com.example.ecommerce.domain.order.model.response.OrderResponse
import com.example.ecommerce.domain.order.repository.OrderTable
import com.example.ecommerce.domain.product.repository.ProductTable
import com.example.ecommerce.domain.user.repository.UserTable
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service

@Service
class OrderService (
     private val orderTable: OrderTable,
     private val userTable: UserTable,
     private val productTable: ProductTable
) {
    /*
     * 주문 등록
     */
    fun addOrder(orderRequest: OrderRequest): OrderResponse {
        val user = userTable.getUser(orderRequest.userId)
            ?: throw IllegalArgumentException("존재하지 않는 사용자입니다.")

        val totalPrice = orderRequest.items.sumOf { item ->
            val product = productTable.getProduct(item.prodId)
                ?: throw IllegalArgumentException("존재하지 않는 상품입니다.")

            product.prodPrice.toDouble() * item.orderQuantity.toDouble()
        }

        if (orderTable.containsOrder(orderRequest.orderId)) {
            throw IllegalArgumentException("이미 존재하는 주문입니다.")
        }

        val order = OrderRequest(
            orderId = orderRequest.orderId,
            userId = user.userId,
            items = orderRequest.items,
            totalPrice = totalPrice,
            orderStatus = "주문 완료"
        )

        orderTable.addOrder(order)

        return OrderResponse(
            orderId = order.orderId,
            userId = order.userId,
            items = order.items,
            totalPrice = order.totalPrice,
            orderStatus = order.orderStatus
        )
    }


    /*
     * 주문 조회
     */
    fun getOrder(userId: Long): List<OrderResponse> {
        val orders = orderTable.getOrdersByUserId(userId)

        if (orders.isEmpty()) {
            throw IllegalArgumentException("해당 사용자의 주문 내역이 없습니다.")
        }

        return orders.map { order ->
            OrderResponse(
                orderId = order.orderId,
                userId = order.userId,
                items = order.items,
                totalPrice = order.totalPrice,
                orderStatus = order.orderStatus
            )
        }
    }

    /*
     * 주문 상태 변경
     */
    fun updateOrderStatus(orderId: Long, newStatus: String) {
        val order = orderTable.getOrder(orderId)
            ?: throw IllegalArgumentException("존재하지 않는 주문입니다.")

        order.updateOrderStatus(newStatus)

        orderTable.addOrder(order) // 업데이트된 주문을 다시 저장
    }
}