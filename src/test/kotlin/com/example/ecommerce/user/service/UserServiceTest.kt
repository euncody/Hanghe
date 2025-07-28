package com.example.ecommerce.user.service

import com.example.ecommerce.user.domain.User
import com.example.ecommerce.user.domain.UserRepository
import com.example.ecommerce.user.domain.UserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    // @InjectMocks 어노테이션을 사용하여 UserService의 인스턴스를 생성하고, 필요한 의존성을 주입
    // Mockito가 UserService의 생성자에 필요한 의존성을 자동으로 주입
    @InjectMocks
    private lateinit var userService: UserService

    private fun createUser(point: Int = 1000): User {
        return User(
            userId = "abc",
            userName = "철수",
            email = "test@example.com",
            point = point
        )
    }

    @Test
    fun `사용자 포인트를 정상적으로 사용한다`() {
        val user = createUser(point = 1000)

        whenever(userRepository.findByUserId("abc")).thenReturn(user)
        whenever(userRepository.save(any())).thenAnswer { it.getArgument<User>(0) }

        val updated = userService.useUserPoint("abc", 500)

        assertEquals(500, updated.point)
    }

    @Test
    fun `사용자 포인트가 부족하면 예외가 발생한다`() {
        val user = createUser(point = 100)

        whenever(userRepository.findByUserId("abc")).thenReturn(user)

        val exception = assertThrows<IllegalStateException> {
            userService.useUserPoint("abc", 500)
        }

        assertEquals("포인트가 부족합니다.", exception.message)
    }

    @Test
    fun `없는 사용자 ID로 포인트를 사용하려 하면 예외가 발생한다`() {
        whenever(userRepository.findByUserId("abc")).thenReturn(null)

        val exception = assertThrows<IllegalArgumentException> {
            userService.useUserPoint("abc", 100)
        }

        assertEquals("사용자를 찾을 수 없습니다.", exception.message)
    }
}