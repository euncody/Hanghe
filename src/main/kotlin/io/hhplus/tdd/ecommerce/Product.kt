package io.hhplus.tdd.ecommerce

data class Product(
    val productCode: String, //상품 코드
    val productName: String, //상품 이름
    val productInfo: String, //상품 정보
    val price:Int, //상품 가격
    val amount : Int //상품 수량
) {
    override fun toString(): String {
        return "Product(productCode='$productCode', " +
                "productName='$productName', " +
                "productInfo='$productInfo', " +
                "price=$price, " +
                "amount=$amount)"
    }
}