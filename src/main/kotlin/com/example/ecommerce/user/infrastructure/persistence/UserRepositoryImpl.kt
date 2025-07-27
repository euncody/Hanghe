package com.example.ecommerce.user.infrastructure.persistence

import com.example.ecommerce.user.UserMapper
import com.example.ecommerce.user.domain.User
import com.example.ecommerce.user.infrastructure.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val userMapper: UserMapper
) : UserRepository {

    override fun findById(userId: Long): User? {
        TODO("Not yet implemented")
    }

    override fun save(user: User): User {
        TODO("Not yet implemented")
    }
}