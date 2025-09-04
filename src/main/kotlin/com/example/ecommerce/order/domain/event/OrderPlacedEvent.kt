package com.example.ecommerce.order.domain.event

data class OrderPlacedEvent(
    val orderId: String,
    val userId: String,
    val totalAmount: Int,
    val productSummaries: List<ProductSummary>
) {
    data class ProductSummary(
        val productKey: Long,
        val quantity: Int,
        val priceAtOrder: Int
    )
}