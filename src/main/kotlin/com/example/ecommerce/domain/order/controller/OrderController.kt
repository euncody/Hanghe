package com.example.ecommerce.domain.order.controller

import com.example.ecommerce.domain.order.model.request.OrderRequest
import com.example.ecommerce.domain.order.model.response.OrderResponse
import com.example.ecommerce.domain.order.service.OrderService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrderController (
    private val orderService: OrderService
) {

    /*
    * 주문 등록
    * */
     @PostMapping
     fun addOrder(@RequestBody orderRequest: OrderRequest): OrderResponse {
         return orderService.addOrder(orderRequest)
     }

    /*
    * 주문 조회
    * */
    @GetMapping("/{userId}")
    fun getOrder(@PathVariable userId: Long): List<OrderResponse> {
        return orderService.getOrder(userId)
    }

    /*
    * 주문 상태 변경
    * */
    @PostMapping("/{orderId}/status")
    fun updateOrderStatus(@PathVariable orderId: Long, @RequestBody newStatus: String): String {
        orderService.updateOrderStatus(orderId, newStatus)
        return "주문 ID $orderId 의 상태가 '$newStatus' 로 변경되었습니다."
    }




}