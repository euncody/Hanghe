package com.example.ecommerce.order.domain

data class OrderItem(
    val orderItemKey: Long = 0,
    val orderItemId: String,
    val productKey: Long,
    val quantity: Int,
    val priceAtOrder: Int
) {
    companion object {
        fun of(orderItemId: String, productKey: Long, quantity: Int, priceAtOrder: Int): OrderItem {
            require(orderItemId.isNotBlank()) { "주문 항목 ID는 비어 있을 수 없습니다." }
            require(productKey > 0) { "상품 키는 0보다 커야 합니다." }
            require(quantity > 0) { "수량은 0보다 커야 합니다." }
            require(priceAtOrder >= 0) { "가격은 0 이상이어야 합니다." }

            return OrderItem(
                orderItemId = orderItemId,
                productKey = productKey,
                quantity = quantity,
                priceAtOrder = priceAtOrder
            )
        }
    }
}
