package com.example.ecommerce.domain.pay.model.request

/*
* 결제 도메인 모델
* */
data class PayRequest(
    val payId: Long, // 결제 ID
    var userId: Long, // 사용자 ID
    var orderId: Long, // 주문 ID
    var amount: Double = 0.0, // 결제 금액
    var paymentMethod: String, // 결제 방법 ("신용카드", "계좌이체", "간편결제")
    var paymentStatus: String = "결제 대기 중" // 결제 상태 ("결제 완료", "결제 취소", "결제 실패" 등
) {

    override fun toString(): String {
        return "Pay(payId=$payId, userId=$userId, orderId=$orderId, amount=$amount, paymentMethod='$paymentMethod')"
    }

    // 결제 한다.
    fun processPayment(userId: Long, orderId: Long, amount: Double, paymentMethod: String) {
        if (userId <= 0 || orderId <= 0 || amount <= 0 || paymentMethod.isBlank()) {
            throw IllegalArgumentException("사용자 ID, 주문 ID, 결제 금액, 결제 방법은 모두 유효한 값이어야 합니다.")
        }

        this.userId = userId
        this.orderId = orderId
        this.amount = amount
        this.paymentMethod = paymentMethod
        this.paymentStatus = "결제 완료"

        println("결제가 성공적으로 처리되었습니다. 결제 ID: $payId, 사용자 ID: $userId, 주문 ID: $orderId, 결제 금액: $amount, 결제 방법: $paymentMethod")
    }

    // 결제 정보를 조회한다.
    fun getPaymentInfo(): String {
        return "결제 ID: $payId, 사용자 ID: $userId, 주문 ID: $orderId, 결제 금액: $amount, 결제 방법: $paymentMethod"
    }

    // 결제 상태를 변경한다.
    fun updatePaymentStatus(newStatus: String) {
        val validStatuses = listOf("결제 완료", "결제 취소", "결제 실패")
        if (newStatus in validStatuses) {
            this.paymentStatus = newStatus
            println("결제 상태가 '$newStatus'로 변경되었습니다.")
        } else {
            throw IllegalArgumentException("유효하지 않은 결제 상태입니다. 가능한 상태: $validStatuses")
        }
    }
}