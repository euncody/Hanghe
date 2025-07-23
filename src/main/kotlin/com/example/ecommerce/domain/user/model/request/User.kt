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

    // 상품을 조회한다.
    fun getProduct(productId: Long) {
        // TODO: 상품 조회 로직
        println("상품 $productId 을(를) 조회했습니다.")
    }


    // 상품을 주문 한다.
    fun purchaseProduct(productId: Long, amount: Int) {
        if (amount > 0) {
            // TODO: 상품 주문 로직
            println("상품 $productId 을(를) $amount 개 구매했습니다.")
        } else {
            throw IllegalArgumentException("구매할 상품의 수량은 0보다 커야 합니다.")
        }
    }

    // 상품을 결제한다.
    fun payForProduct(productId: Long, amount: Int) {
        if (amount > 0) {
            // TODO: 상품 결제 로직
            println("상품 $productId 을(를) $amount 원에 결제했습니다.")
        } else {
            throw IllegalArgumentException("결제 금액은 0보다 커야 합니다.")
        }
    }












}