package com.example.ecommerce.product.web

import com.example.ecommerce.product.domain.Product

data class ProductRequest(
    val productCode: String,
    val productName: String,
    val productInfo: String? = null,
    val price: Int,
    val amount: Int
) {
    fun toDomain() : Product {
        return Product(
            productCode = productCode,
            productName = productName,
            productInfo = productInfo,
            price = price,
            amount = amount
        )
    }
}