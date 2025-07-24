package com.example.ecommerce.domain.order.model.response

import com.example.ecommerce.domain.order.model.request.OrderItemRequest

data class OrderResponse (
    val orderId: Long, // 주문 ID
    val userId: Long, // 사용자 ID
    var items : List<OrderItemRequest>, // 제품 ID 목록
    val totalPrice: Double, // 총 금액
    val orderStatus: String // 주문 상태 ("주문 완료", "주문 취소", "배송 중" 등)
)