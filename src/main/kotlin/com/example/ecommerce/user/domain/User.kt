package com.example.ecommerce.user.domain

import java.time.LocalDateTime

// 도메인 모델
// -> 비즈니스 개념과 행위(= 유즈케이스 내 규칙)를 함께 캡슐화
data class User(
    val userKey: Long? = null,
    val userId: String,
    val userName: String,
    val phone: String? = null,
    val email: String,
    val useState: String = "Y",
    val hasCoupon: String = "N",
    val registDate: LocalDateTime? = null,
    val modiDate: LocalDateTime? = null,
    val deleteDate: LocalDateTime? = null,
    var point: Int = 0
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