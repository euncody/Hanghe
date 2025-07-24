package com.example.ecommerce.domain.order.model.request

data class OrderItemRequest (
    val prodId : Long, // 제품 ID
    val orderQuantity: Int // 주문 수량
) {

    // 주문 항목을 등록한다.
    fun registerOrderItem(prodId: Long, orderQuantity: Int) {
        if (prodId <= 0 || orderQuantity <= 0) {
            throw IllegalArgumentException("제품 ID와 주문 수량은 모두 0보다 큰 값이어야 합니다.")
        }
    }

    // 주문 항목 정보를 조회한다.
    fun getOrderItemInfo(): String {
        return "제품 ID: $prodId, 주문 수량: $orderQuantity"
    }
}