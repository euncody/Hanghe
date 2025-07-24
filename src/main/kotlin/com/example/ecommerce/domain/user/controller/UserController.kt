package com.example.ecommerce.domain.user.controller

import com.example.ecommerce.domain.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {

    /*
     * 사용자 조회
     * */
    @GetMapping("/{id}")
    fun getUserInfo(@PathVariable id: Long): String {
        return userService.getUserInfo(id).toString()
    }

    /*
     * 사용자 포인트 충전
     * */
    @GetMapping("/{id}/charge")
    fun chargePoint(@PathVariable id: Long, @RequestParam amount: Int): String {
        userService.chargePoint(id, amount)
        return "사용자 $id 의 포인트를 $amount 만큼 충전했습니다."
    }

    /*
     * 사용자 포인트 사용
     * */
    @GetMapping("/{id}/use")
    fun usePoint(@PathVariable id: Long, @RequestParam amount: Int): String {
        userService.usePoint(id, amount)
        return "사용자 $id 의 포인트를 $amount 만큼 사용했습니다."
    }
}