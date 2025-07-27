package com.example.ecommerce.user.domain

// 핵심 도메인 로직
data class User (
    val userId: Long,
    val name: String,
    var point : Int = 0
) {

    fun chargePoint(amount: Int): User {
        require(amount > 0) { "충전 금액은 0보다 커야 합니다." }
        return this.copy(point = this.point + amount)
    }

    fun usePoint(amount: Int): User {
        require(amount > 0) { "사용 금액은 0보다 커야 합니다." }
        require(this.point >= amount) { "포인트가 부족합니다." }
        return this.copy(point = this.point - amount)
    }

}