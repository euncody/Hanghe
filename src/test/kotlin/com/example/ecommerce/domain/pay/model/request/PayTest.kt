package com.example.ecommerce.domain.pay.model.request

import com.example.ecommerce.domain.pay.model.repository.PayTable
import org.junit.jupiter.api.Test

class PayTest {

    private lateinit var payTable: PayTable

    @Test
    fun `결제 정보를 등록하고 조회한다`() {
        // given
        payTable = PayTable()
        val pay = Pay(
            payId = 1L,
            userId = 1L,
            orderId = 1L,
            amount = 300.0,
            paymentMethod = "신용카드",
            paymentStatus = "결제 완료"
        )

        // when
        payTable.addPay(pay)

        // then
        assert(payTable.containsPay(1L)) { "결제 정보가 등록되지 않았습니다." }

        val retrievedPay = payTable.getPay(1L)
        assert(retrievedPay != null) { "결제 정보 조회 결과가 null입니다." }

        assert(retrievedPay!!.paymentStatus == "결제 완료") { "결제 상태가 '결제 완료'가 아닙니다." }
    }

    @Test
    fun `결제 완료 후 상태는 결제 완료여야 한다`() {
        // given
        payTable = PayTable()
        val pay = Pay(
            payId = 2L,
            userId = 2L,
            orderId = 2L,
            amount = 500.0,
            paymentMethod = "계좌이체",
            paymentStatus = "결제 대기 중"
        )

        // when
        pay.processPayment(userId = 2L, orderId = 2L, amount = 500.0, paymentMethod = "계좌이체")

        // then
        assert(pay.paymentStatus == "결제 완료") { "결제 상태가 '결제 완료'가 아닙니다." }
    }
}