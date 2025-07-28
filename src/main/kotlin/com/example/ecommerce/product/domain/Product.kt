package com.example.ecommerce.product.domain

data class Product(
    val productKey: Long? = null,
    val productCode: String,
    val productName: String,
    val productInfo: String?,
    val price: Int,
    val amount: Int
) {

    fun decreaseStock(quantity: Int): Product {
        require(quantity > 0) { "차감 수량은 0보다 커야 합니다." }
        require(this.amount >= quantity) { "재고가 부족합니다." }
        return this.copy(amount = this.amount - quantity)
    }

    fun increaseStock(quantity: Int): Product {
        require(quantity > 0) { "추가 수량은 0보다 커야 합니다." }
        return this.copy(amount = this.amount + quantity)
    }
}