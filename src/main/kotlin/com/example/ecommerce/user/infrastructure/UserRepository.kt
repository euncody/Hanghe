package com.example.ecommerce.user.infrastructure

import com.example.ecommerce.user.domain.User

interface UserRepository {

    fun findById(userId: Long): User?
    fun save(user: User): User
}