package com.example.ecommerce.domain.pay.repository

import com.example.ecommerce.domain.pay.model.request.PayRequest
import org.springframework.stereotype.Repository

@Repository
class PayTable {
    private val payRequestTable = HashMap<Long, PayRequest>()

    fun addPay(payRequest: PayRequest) {
        payRequestTable[payRequest.payId] = payRequest
    }

    fun getPay(id: Long): PayRequest? {
        return payRequestTable[id]
    }

    fun getAllPays(): List<PayRequest> {
        return payRequestTable.values.toList()
    }

    fun containsPay(id: Long): Boolean {
        return payRequestTable.containsKey(id)
    }
}