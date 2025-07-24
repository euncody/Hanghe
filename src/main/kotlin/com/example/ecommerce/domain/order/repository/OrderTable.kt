package com.example.ecommerce.domain.order.repository

import com.example.ecommerce.domain.order.model.request.OrderRequest
import org.springframework.stereotype.Repository

@Repository
class OrderTable {
    private val orderRequestTable = HashMap<Long, OrderRequest>()

    fun addOrder(orderRequest: OrderRequest) {
        orderRequestTable[orderRequest.orderId] = orderRequest
    }

    fun getOrder(id: Long): OrderRequest? {
        return orderRequestTable[id]
    }

    fun getOrdersByUserId(userId: Long): List<OrderRequest> {
        return orderRequestTable.values.filter { it.userId == userId }
    }

    fun getAllOrders(): List<OrderRequest> {
        return orderRequestTable.values.toList()
    }

    fun containsOrder(id: Long): Boolean {
        return orderRequestTable.containsKey(id)
    }
}