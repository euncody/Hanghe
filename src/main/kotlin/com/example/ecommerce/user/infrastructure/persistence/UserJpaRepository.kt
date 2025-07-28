package com.example.ecommerce.user.infrastructure.persistence

import com.example.ecommerce.user.infrastructure.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<UserEntity, Long> {
    fun findByUserId(userId: String): UserEntity?
}