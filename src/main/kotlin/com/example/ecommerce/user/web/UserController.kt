package com.example.ecommerce.user.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// 클라이언트 요청을 받아 처리하는 API 엔드포인트

@RestController
@RequestMapping("/users")
class UserController {

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): String {
        return "user $id"
    }

}