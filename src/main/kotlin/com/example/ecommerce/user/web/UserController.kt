package com.example.ecommerce.user.web

import com.example.ecommerce.user.domain.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// 클라이언트 요청을 받아 처리하는 API 엔드포인트

@RestController
@RequestMapping("/api/users")
class UserController (
    private val userService: UserService
) {

    // 사용자 정보를 조회하는 API
    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: String): UserResponse {
        val user = userService.getUserByUserId(userId)
        return UserResponse.from(user)
    }

    // 사용자 등록 API
    @PostMapping
    fun registerUser(@RequestBody request: UserRequest): UserResponse {
        val user = request.toDomain()
        val savedUser = userService.registerUser(user)
        return UserResponse.from(savedUser)
    }

    // 사용자 포인트 충전 API
    @PostMapping("/{userId}/point")
    fun chargePoint(
        @PathVariable userId: String,
        @RequestBody request: UserPointRequest
    ): UserResponse {
        val user = userService.chargeUserPoint(userId, request.point)
        return UserResponse.from(user)
    }

    // 사용자 포인트 사용 API
    @PostMapping("/{userId}/use-point")
    fun usePoint(
        @PathVariable userId: String,
        @RequestBody request: UserPointRequest
    ): UserResponse {
        val user = userService.useUserPoint(userId, request.point)
        return UserResponse.from(user)
    }




}