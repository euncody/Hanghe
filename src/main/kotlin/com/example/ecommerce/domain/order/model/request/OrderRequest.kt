package com.example.ecommerce.domain.order.model.request

/*
* 주문 도메인 모델
* */
data class OrderRequest(
    val orderId: Long, // 주문 ID
    var userId: Long, // 사용자 ID
    var items : List<OrderItemRequest> = listOf(), // 주문 항목 목록
    var totalPrice: Double = 0.0,
    var orderStatus : String // 주문 상태 ("주문 완료", "주문 취소", "배송 중")
) {


    // 주문을 등록한다.
    fun registerOrder(userId : Long, prodId: Long, quantity: Int) {
        if (userId <= 0 || prodId <= 0 || quantity <= 0) {
            throw IllegalArgumentException("사용자 ID, 제품 ID, 수량은 모두 0보다 큰 값이어야 합니다.")
        }
        this.userId = userId
        this.items = listOf(OrderItemRequest(prodId, quantity))
        this.orderStatus = "주문 완료"
    }

    // 주문 정보를 조회한다.
    fun getOrderInfo(userId: Long): String {
        if (userId != this.userId) {
            throw IllegalArgumentException("사용자 ID가 일치하지 않습니다.")
        }

        return "주문 ID: $orderId, 사용자 ID: $userId, 제품 목록: ${items.map { it.prodId }}, 총 금액: $totalPrice, 주문 상태: $orderStatus"
    }

    // 주문 상태를 변경한다.
    fun updateOrderStatus(newStatus: String) {
        val validStatuses = listOf("주문 완료", "주문 취소", "배송 중")
        if (newStatus in validStatuses) {
            this.orderStatus = newStatus
        } else {
            throw IllegalArgumentException("유효하지 않은 주문 상태입니다. 가능한 상태: $validStatuses")
        }
    }






}