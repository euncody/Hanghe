package com.example.ecommerce.domain.pay.model.repository

import com.example.ecommerce.domain.pay.model.request.Pay

class PayTable {
    private val payTable = HashMap<Long, Pay>()

    fun addPay(pay: Pay) {
        payTable[pay.payId] = pay
    }

    fun getPay(id: Long): Pay? {
        return payTable[id]
    }

    fun getAllPays(): List<Pay> {
        return payTable.values.toList()
    }

    fun containsPay(id: Long): Boolean {
        return payTable.containsKey(id)
    }
}