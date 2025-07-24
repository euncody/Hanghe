package com.example.ecommerce.domain.user.model.response

data class UserResponse (
    val userId: Long,
    val name: String,
    var point: Int
)