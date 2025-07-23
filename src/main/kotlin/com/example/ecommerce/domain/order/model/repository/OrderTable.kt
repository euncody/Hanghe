package com.example.ecommerce.domain.order.model.repository

import com.example.ecommerce.domain.order.model.request.Order

class OrderTable {
    private val orderTable = HashMap<Long, Order>()

    fun addOrder(order: Order) {
        orderTable[order.orderId] = order
    }

    fun getOrder(id: Long): Order? {
        return orderTable[id]
    }

    fun getAllOrders(): List<Order> {
        return orderTable.values.toList()
    }

    fun containsOrder(id: Long): Boolean {
        return orderTable.containsKey(id)
    }
}