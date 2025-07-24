package com.example.ecommerce.domain.product.model.response

data class ProductResponse (
    val prodId: Long,
    val prodName: String,
    val prodPrice: Double,
    var prodDescription: String?,
    var prodStock: Int
)