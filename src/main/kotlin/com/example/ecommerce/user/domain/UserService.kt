package com.example.ecommerce.user.domain

import com.example.ecommerce.user.UserMapper
import com.example.ecommerce.user.infrastructure.UserRepository
import com.example.ecommerce.user.web.UserResponse
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) {

    fun chargePoint(userId: Long, amount: Int): UserResponse {
        val user = userRepository.findById(userId)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")

        val updated = user.chargePoint(amount)
        val saved = userRepository.save(updated)

        return userMapper.toResponse(saved)
    }

    fun usePoint(userId: Long, amount: Int): UserResponse {
        val user = userRepository.findById(userId)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")

        val updated = user.usePoint(amount)
        val saved = userRepository.save(updated)

        return userMapper.toResponse(saved)
    }

    fun getUser(userId: Long): UserResponse {
        val user = userRepository.findById(userId)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")
        return userMapper.toResponse(user)
    }
}