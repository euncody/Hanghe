package com.example.ecommerce.domain.pay.controller

import com.example.ecommerce.domain.order.service.OrderService
import com.example.ecommerce.domain.pay.model.request.PayRequest
import com.example.ecommerce.domain.pay.model.response.PayResponse
import com.example.ecommerce.domain.pay.service.PayService
import com.example.ecommerce.domain.product.service.ProductService
import com.example.ecommerce.domain.user.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/pays")
class PayController (
    private val payService : PayService,
    private val userService: UserService,
    private val orderService: OrderService
) {

    /*
     * 결제 수단 등록
     */
    @PostMapping("/reg")
    fun addPayment(@RequestBody payRequest: PayRequest): PayResponse {
        val user = userService.getUserInfo(payRequest.userId)
        return payService.addPayment(payRequest)
    }
    

    /*
    * 결제 하기
    * */
    @PostMapping("/uses")
    fun processPayment(@RequestBody payRequest: PayRequest): PayResponse {
        val user = userService.getUserInfo(payRequest.userId)
        val order = orderService.getOrder(payRequest.orderId)

        val hasEnoughPoints = user.point >= payRequest.amount

        if (hasEnoughPoints) {
            return payService.processPayment(payRequest)
        } else {
            throw IllegalArgumentException("보유 포인트가 부족합니다. 현재 포인트: ${user.point}, 결제 금액: ${payRequest.amount}")
        }
    }

}