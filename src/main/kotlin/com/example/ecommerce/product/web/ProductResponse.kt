package com.example.ecommerce.product.web

import com.example.ecommerce.product.domain.Product

data class ProductResponse(
    val productCode: String,
    val productName: String,
    val productInfo: String?,
    val price: Int,
    val amount: Int
) {
    companion object {
        fun from(product: Product): ProductResponse {
            return ProductResponse(
                productCode = product.productCode,
                productName = product.productName,
                productInfo = product.productInfo,
                price = product.price,
                amount = product.amount
            )
        }
    }
}