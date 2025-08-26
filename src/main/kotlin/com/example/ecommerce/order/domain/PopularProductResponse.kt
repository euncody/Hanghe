package com.example.ecommerce.order.domain

data class PopularProductResponse(
    val productKey: Long,
    val productCode: String,
    val productName: String,
    val totalQty: Long,
    val totalRev: Long
)