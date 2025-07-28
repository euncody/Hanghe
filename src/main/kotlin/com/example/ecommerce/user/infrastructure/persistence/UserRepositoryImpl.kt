package com.example.ecommerce.user.infrastructure.persistence

import com.example.ecommerce.user.UserMapper
import com.example.ecommerce.user.domain.User
import com.example.ecommerce.user.domain.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val jpaRepository: UserJpaRepository,
    private val userMapper: UserMapper
) : UserRepository {

    override fun findByUserId(userId: String): User? {
        val entity = jpaRepository.findByUserId(userId)
        return entity?.let { userMapper.toDomain(it) }
    }

    override fun save(user: User): User {
        val entity = userMapper.toEntity(user)
        val savedEntity = jpaRepository.save(entity)
        return userMapper.toDomain(savedEntity)
    }



}