package com.example.ecommerce.user.web

import com.example.ecommerce.user.domain.User

// 서버 → 클라이언트 응답 시 사용하는 데이터 전달 객체 (DTO)
data class UserResponse(
    val userId: String,
    val userName: String,
    val phone: String?,
    val email: String,
    val point: Int
) {
    companion object {
        fun from(user: User): UserResponse {
            return UserResponse(
                userId = user.userId,
                userName = user.userName,
                phone = user.phone,
                email = user.email,
                point = user.point
            )
        }
    }
}