package com.example.ecommerce.order.web

import com.example.ecommerce.order.domain.OrderRepository
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository
) {
}