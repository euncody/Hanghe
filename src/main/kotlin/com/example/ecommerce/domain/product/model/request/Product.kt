package com.example.ecommerce.domain.product.model.request

/*
* 제품 도메인 모델
* */
data class Product (
    val prodId: Long, // 제품 ID
    var prodName: String, // 제품 이름
    var prodPrice: Double, // 제품 가격
    var prodDescription: String? = null, // 제품 설명
    var prodStock : Int = 0 // 제품 재고 수량
) {

    override fun toString(): String {
        return "Product(prodId=$prodId, prodName='$prodName', prodPrice=$prodPrice, prodDescription=$prodDescription)"
    }

    // 제품 목록을 조회한다.
    fun getProductInfo(): String {
        return "제품 ID: $prodId, 이름: $prodName, 가격: $prodPrice, 설명: $prodDescription"
    }

    // prodId로 제품을 조회한다.
    fun getProductById(id: Long): String {
        if (id == this.prodId) {
            return "제품 ID: $prodId, 이름: $prodName, 가격: $prodPrice, 설명: $prodDescription"
        } else {
            throw IllegalArgumentException("제품 ID가 일치하지 않습니다.")
        }
    }

    // 제품을 등록한다.
    fun registerProduct(name: String, price: Double, description: String? = null, stock: Int = 0) {
        if (name.isNotBlank() && price > 0) {
            this.prodName = name
            this.prodPrice = price
            this.prodDescription = description
            this.prodStock = stock
        } else {
            throw IllegalArgumentException("제품 이름은 비어있을 수 없고, 가격은 0보다 커야 합니다.")
        }

        println("제품이 성공적으로 등록되었습니다. 제품 ID: $prodId, 이름: $prodName, 가격: $prodPrice, 설명: $prodDescription, 재고: $prodStock")
    }

    // 제품의 가격을 수정한다.
    fun updateProductPrice(newPrice: Double) {
        if (newPrice > 0) {
            this.prodPrice = newPrice
        } else {
            throw IllegalArgumentException("가격은 0보다 큰 금액이어야 합니다.")
        }

        println("제품 가격이 $newPrice 로 변경되었습니다.")
    }

    // 제품의 재고를 수정한다.
    fun updateProductStock(newStock: Int) {
        if (newStock >= 0) {
            // TODO : 재고 처리 인지 추가 인지 처리 필요
            this.prodStock = this.prodStock - newStock
        } else {
            throw IllegalArgumentException("재고 수량은 음수일 수 없습니다.")
        }

        println("제품 재고가 $newStock 로 변경되었습니다. 현재 재고: $prodStock")
    }

}