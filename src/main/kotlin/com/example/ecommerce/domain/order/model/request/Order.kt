package com.example.ecommerce.domain.order.model.request

/*
* 주문 도메인 모델
* */
data class Order(
    val orderId: Long, // 주문 ID
    var id: Long, // 사용자 ID
    var prodId: Long, // 제품 ID
    var orderQuantity: Int = 1, // 주문 수량
    var totalPrice: Double = 0.0,
    var orderStatus : String // 주문 상태 ("주문 완료", "주문 취소", "배송 중")
) {

    override fun toString(): String {
        return "Order(orderId=$orderId, userId=$id, productId=$prodId, quantity=$orderQuantity, totalPrice=$totalPrice)"
    }

    // 주문을 등록한다.
    fun registerOrder(id : Long, prodId: Long, quantity: Int) {
        if (id <= 0 || prodId <= 0 || quantity <= 0) {
            throw IllegalArgumentException("사용자 ID, 제품 ID, 수량은 모두 0보다 큰 값이어야 합니다.")
        }

        this.orderQuantity = quantity
        this.id = id
        this.prodId = prodId
    }

    // 주문 정보를 조회한다.
    fun getOrderInfo(id: Long): String {
        if (id != this.id) {
            throw IllegalArgumentException("사용자 ID가 일치하지 않습니다.")
        }

        return "주문 ID: $orderId, 사용자 ID: $id, 제품 ID: $prodId, 수량: $orderQuantity, 총 금액: $totalPrice"
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