package com.example.ecommerce.user.domain

import com.example.ecommerce.global.lock.RedisLock
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun getUserByUserId(userId: String): User {
        return userRepository.findByUserId(userId)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")
    }

    @Transactional
    fun registerUser(user: User): User {
        val saved = userRepository.save(user)
        return saved
    }

    @Transactional
    @RedisLock(key = "user:point:#{#userId}", waitTimeMs = 2000, leaseTimeMs = 5000)
    fun chargeUserPoint(userId: String, point: Int): User {
        val user = userRepository.findByUserId(userId)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")

        val updated = user.chargePoint(point)
        return userRepository.save(updated)
    }

    @Transactional
    @RedisLock(key = "user:point:#{#userId}", waitTimeMs = 2000, leaseTimeMs = 5000)
    fun useUserPoint(userId: String, point: Int): User {
        val user = userRepository.findByUserId(userId)
            ?: throw IllegalArgumentException("사용자를 찾을 수 없습니다.")

        val updated = user.usePoint(point)
        return userRepository.save(updated)
    }


}