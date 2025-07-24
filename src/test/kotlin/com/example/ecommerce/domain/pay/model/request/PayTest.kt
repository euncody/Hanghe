package com.example.ecommerce.domain.pay.model.request

import com.example.ecommerce.domain.pay.repository.PayTable
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

class PayTest {

    private lateinit var payTable: PayTable

    @Test
    fun `결제 정보를 등록하고 조회한다`() {
        // given
        payTable = PayTable()
        val payRequest = PayRequest(
            payId = 1L,
            userId = 1L,
            orderId = 1L,
            amount = 300.0,
            paymentMethod = "신용카드",
            paymentStatus = "결제 완료"
        )

        // when
        payTable.addPay(payRequest)

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
        val payRequest = PayRequest(
            payId = 2L,
            userId = 2L,
            orderId = 2L,
            amount = 500.0,
            paymentMethod = "계좌이체",
            paymentStatus = "결제 대기 중"
        )

        // when
        payRequest.processPayment(userId = 2L, orderId = 2L, amount = 500.0, paymentMethod = "계좌이체")

        // then
        assert(payRequest.paymentStatus == "결제 완료") { "결제 상태가 '결제 완료'가 아닙니다." }
    }

    @Test
    fun `결제 상태를 변경할 수 있다`() {
        val payRequest = PayRequest(
            payId = 4L,
            userId = 4L,
            orderId = 4L,
            amount = 400.0,
            paymentMethod = "간편결제",
            paymentStatus = "결제 대기 중"
        )

        payRequest.updatePaymentStatus("결제 취소")
        assertEquals("결제 취소", payRequest.paymentStatus)
    }

    @Test
    fun `잘못된 결제 상태 변경 시 예외 발생`() {
        val payRequest = PayRequest(
            payId = 5L,
            userId = 5L,
            orderId = 5L,
            amount = 500.0,
            paymentMethod = "신용카드"
        )

        val exception = assertThrows<IllegalArgumentException> {
            payRequest.updatePaymentStatus("무효상태")
        }

        assertTrue(exception.message!!.contains("유효하지 않은 결제 상태입니다."))
    }

    @Test
    fun `결제 정보 문자열을 확인한다`() {
        val payRequest = PayRequest(
            payId = 6L,
            userId = 6L,
            orderId = 6L,
            amount = 600.0,
            paymentMethod = "계좌이체"
        )

        val info = payRequest.getPaymentInfo()
        assertTrue(info.contains("결제 ID: 6"))
        assertTrue(info.contains("사용자 ID: 6"))
        assertTrue(info.contains("결제 금액: 600.0"))
    }

}