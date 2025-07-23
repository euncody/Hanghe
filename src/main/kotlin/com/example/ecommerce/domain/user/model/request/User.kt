package com.example.ecommerce.domain.user.model.request

/*
* 사용자 도메인 모델
* */
data class User(
    val id: Long,
    val name: String,
    var point : Int = 0
) {

    fun User(
        id: String,
        name: String,
        point: Int = 0
    ) : User {
        return User(id, name, point)
    }

    override fun toString(): String {
        return "User(id='$id', name='$name', point=$point)"
    }


    // 포인트를 충전한다.
    fun chargePoint(amount: Int) {
        if (amount > 0) {
            this.point += amount
        } else {
            throw IllegalArgumentException("포인트는 0보다 큰 금액이어야 합니다.")
        }
    }

    // 포인트를 사용한다.
    fun usePoint(amdount : Int) {
        if (amdount > 0 && amdount <= this.point) {
            this.point -= amdount
        } else {
            throw IllegalArgumentException("사용할 수 있는 포인트가 부족합니다.")
        }
    }

    // 포인트를 조회한다.
    fun getPoint(id: Long) : Int {
        if( id != this.id) {
            throw IllegalArgumentException("사용자 ID가 일치하지 않습니다.")
        } else {
            println("사용자 $id 의 포인트는 ${this.point} 입니다.")
        }

        return this.point;
    }
}