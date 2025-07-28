package com.example.ecommerce.user.domain

interface UserRepository {
    fun findByUserId(userId: String): User?
    fun save(user: User): User
}