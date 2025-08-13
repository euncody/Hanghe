package com.example.ecommerce.product.domain

import com.example.ecommerce.global.lock.RedisLock

data class Product(
    val productKey: Long? = null,
    val productCode: String,
    val productName: String,
    val productInfo: String?,
    val price: Int,
    val amount: Int
) {

    @RedisLock(key = "product:stock:#{#productCode}", waitTimeMs = 3000, leaseTimeMs = 8000)
    fun decreaseStock(quantity: Int): Product {
        require(quantity > 0) { "차감 수량은 0보다 커야 합니다." }
        require(this.amount >= quantity) { "재고가 부족합니다." }
        return this.copy(amount = this.amount - quantity)
    }

    @RedisLock(key = "product:stock:#{#productCode}", waitTimeMs = 3000, leaseTimeMs = 8000)
    fun increaseStock(quantity: Int): Product {
        require(quantity > 0) { "추가 수량은 0보다 커야 합니다." }
        return this.copy(amount = this.amount + quantity)
    }
}