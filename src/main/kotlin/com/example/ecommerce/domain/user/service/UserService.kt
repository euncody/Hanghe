package com.example.ecommerce.domain.user.service

import com.example.ecommerce.domain.user.model.response.UserResponse
import com.example.ecommerce.domain.user.repository.UserTable
import org.springframework.stereotype.Service

@Service
class UserService (
    private val userTable: UserTable
) {

    /*
    * 사용자 조회
    * */
    fun getUserInfo(userId: Long): UserResponse {
        val user = userTable.getUser(userId)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")

        return UserResponse(
            userId = user.userId,
            name = user.name,
            point = user.point
        )
    }

    /*
    * 사용자 포인트 충전
    * */
    fun chargePoint(userId: Long, amount: Int) {
        val user = userTable.getUser(userId)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")

        if (amount <= 0) {
            throw IllegalArgumentException("충전 금액은 0보다 커야 합니다.")
        }

        user.chargePoint(amount)
        userTable.updateUser(user)
    }


    /*
     * 사용자 포인트 사용
     * */
    fun usePoint(userId: Long, amount: Int) {
        val user = userTable.getUser(userId)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")

        if (amount <= 0 || amount > user.point) {
            throw IllegalArgumentException("사용할 수 있는 포인트가 부족합니다.")
        }

        user.usePoint(amount)
        userTable.updateUser(user)
    }
}