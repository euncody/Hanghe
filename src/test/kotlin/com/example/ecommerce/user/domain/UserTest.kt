package com.example.ecommerce.user.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserTest {

    @Test
    fun `포인트를 정상적으로 충전한다`() {
        val user = User(
            userId = "abc",
            userName = "철수",
            email = "test@example.com",
            point = 1000)

        val updated = user.chargePoint(500)

        assertEquals(1500, updated.point)
    }


    @Test
    fun `0 이하의 금액으로 포인트를 충전하면 예외가 발생한다`() {
        val user = User(
            userId = "abc",
            userName = "철수",
            email = "test@example.com",
            point = 1000 // 정상 상태로 생성
        )

        val exception = assertThrows<IllegalArgumentException> {
            user.chargePoint(0)
        }

        assertEquals("충전 금액은 0보다 커야 합니다.", exception.message)
    }


    @Test
    fun `포인트를 초과 사용하면 예외가 발생한다`() {
        val user = User(
            userId = "abc",
            userName = "철수",
            email = "test@example.com",
            point = 100)

        val exception = assertThrows<IllegalStateException> {
            user.usePoint(200)
        }

        assertEquals("포인트가 부족합니다.", exception.message)
    }
}