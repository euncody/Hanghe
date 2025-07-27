package com.example.ecommerce.domain.pay.service

import com.example.ecommerce.domain.pay.model.request.PayRequest
import com.example.ecommerce.domain.pay.model.response.PayResponse
import com.example.ecommerce.domain.pay.repository.PayTable
import com.example.ecommerce.domain.user.repository.UserTable
import org.springframework.stereotype.Service

@Service
class PayService (
    private val payTable : PayTable
) {

    /*
     * 결제 수단 등록
     */
    fun addPayment(payRequest: PayRequest): PayResponse {
        if (payTable.containsPay(payRequest.payId)) {
            throw IllegalArgumentException("이미 등록된 결제 ID입니다: ${payRequest.payId}")
        }

        payTable.addPay(payRequest)

        return PayResponse(
            payId = payRequest.payId,
            userId = payRequest.userId,
            orderId = payRequest.orderId,
            amount = payRequest.amount,
            paymentMethod = payRequest.paymentMethod,
            paymentStatus = payRequest.paymentStatus
        )
    }

    /*
    * 상품 결제
    * */
    fun processPayment(payRequest: PayRequest): PayResponse {
        val existingPay = payTable.getPay(payRequest.payId)
            ?: throw IllegalArgumentException("결제 정보가 존재하지 않습니다. payId: ${payRequest.payId}")

        existingPay.processPayment(payRequest.userId,
            payRequest.orderId, payRequest.amount, payRequest.paymentMethod)

        return PayResponse(
            payId = existingPay.payId,
            userId = existingPay.userId,
            orderId = existingPay.orderId,
            amount = existingPay.amount,
            paymentMethod = existingPay.paymentMethod,
            paymentStatus = existingPay.paymentStatus
        )
    }






}