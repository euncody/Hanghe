package com.example.ecommerce.order.web

import com.example.ecommerce.order.domain.Order
import com.example.ecommerce.order.domain.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService
) {

    @GetMapping
    fun getAllOrders(): ResponseEntity<List<Order>> {
        return ResponseEntity.ok(orderService.getAllOrders())
    }

    @GetMapping("/{orderId}")
    fun getOrderById(@PathVariable orderId: String): ResponseEntity<Order> {
        val order = orderService.getOrderById(orderId)
        return if (order != null) ResponseEntity.ok(order)
        else ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createOrder(@RequestBody order: Order): ResponseEntity<Order> {
        val saved = orderService.createOrder(order)
        return ResponseEntity.status(HttpStatus.CREATED).body(saved)
    }

    @PutMapping("/{orderId}")
    fun updateOrder(
        @PathVariable orderId: String,
        @RequestBody order: Order
    ): ResponseEntity<Order> {
        // 도메인 구조에 따라 검증 로직 추가 가능
        val updated = orderService.updateOrder(order)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{orderId}")
    fun deleteOrder(@PathVariable orderId: String): ResponseEntity<Void> {
        orderService.deleteOrder(orderId)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/user/{userId}")
    fun getOrdersByUserId(@PathVariable userId: String): ResponseEntity<List<Order>> {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId))
    }

    @GetMapping("/product/{productCode}")
    fun getOrdersByProductCode(@PathVariable productCode: String): ResponseEntity<List<Order>> {
        return ResponseEntity.ok(orderService.getOrdersByProductKey(productCode))
    }
}
