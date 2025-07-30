package com.example.ecommerce.order.web

import com.example.ecommerce.order.domain.Order
import com.example.ecommerce.order.domain.OrderRepository
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository
) {
    fun getOrderById(orderId: String) = orderRepository.findByOrderId(orderId)

    fun createOrder(order: Order) = orderRepository.save(order)

    fun deleteOrder(orderId: String) = orderRepository.delete(orderId)

    fun getAllOrders() = orderRepository.findAll()

    fun getOrdersByUserId(userId: String) = orderRepository.findByUserId(userId)

    fun updateOrder(order: Order) = orderRepository.update(order)

    fun getOrdersByProductCode(productCode: String) = orderRepository.findByProductCode(productCode)
}