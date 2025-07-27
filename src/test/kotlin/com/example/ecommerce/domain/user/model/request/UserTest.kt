package com.example.ecommerce.domain.user.model.request

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserTest {

    private lateinit var userRequest: UserRequest

    // given
    @BeforeEach
    fun setUp() {
        userRequest = UserRequest(userId = 1L, name = "User1", point = 0)
    }

    /*
    * 포인트 테스트
    * */
    @Test
    fun `0보다 큰 금액이면 포인트를 적립한다`() {
//        // given
//        val user = User(id = 1L, name = "User1", point = 0);
        // when
        userRequest.chargePoint(100)

        /*
        * STUDY: assertEquals(a, b)
        * a와 b가 같으면 테스트 통과
        *
        * STUDY: assertThrows(IllegalArgumentException::class.java) { ... }
        * IllegalArgumentException 예외가 발생해야 테스트 통과
        * */

        // then
        Assertions.assertEquals(100, userRequest.getPoint(userRequest.userId))
    }


    @Test
    fun `0보다 큰 금액이 아니면 포인트를 적립할 수 없다`() {
        // given
        //val user = User(id = 1L, name = "User1", point = 0);

        // when & then
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            userRequest.chargePoint(-100)
        }
    }

    @Test
    fun `포인트를 사용한다`() {
        // given
        //val user = User(id = 1L, name = "User1", point = 100);

        // when
        userRequest.chargePoint(100) // 먼저 포인트를 충전
        userRequest.usePoint(50)

        // then
        Assertions.assertEquals(50, userRequest.getPoint(userRequest.userId))
    }


}