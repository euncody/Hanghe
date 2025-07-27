package com.example.ecommerce.domain.pay.model.response

data class PayResponse (
    val payId: Long, // 결제 ID
    var userId: Long, // 사용자 ID
    var orderId: Long, // 주문 ID
    var amount: Double = 0.0, // 결제 금액
    var paymentMethod: String, // 결제 방법 ("신용카드", "계좌이체", "간편결제")
    var paymentStatus: String = "결제 대기 중" // 결제 상태 ("결제 완료", "결제 취소", "결제 실패" 등)
)