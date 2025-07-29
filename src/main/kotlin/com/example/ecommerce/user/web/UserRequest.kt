package com.example.ecommerce.user.web

import com.example.ecommerce.user.domain.User

// 클라이언트 → 서버 요청 시 사용하는 데이터 전달 객체 (DTO)
data class UserRequest(
    val userId: String = "",
    val userName: String = "",
    val phone: String? = null,
    val email: String = "",
    val point: Int = 0
) {
    fun toDomain(): User {
        return User(
            userId = userId, // TODO : "U-" 접두사 추가
            userName = userName,
            phone = phone,
            email = email,
            point = point
        )
    }
}
