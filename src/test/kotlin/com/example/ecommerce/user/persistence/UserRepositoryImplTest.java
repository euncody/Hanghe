package com.example.ecommerce.user.persistence;

import com.example.ecommerce.user.UserMapper
import com.example.ecommerce.user.domain.User
import com.example.ecommerce.user.domain.UserRepository
import com.example.ecommerce.user.infrastructure.persistence.UserRepositoryImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
@Import(UserRepositoryImpl::class, UserMapper::class)
class UserRepositoryImplTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    private fun createUser(
            userId: String = "abc",
            userName: String = "테스트유저",
            point: Int = 1000
    ): User = User(userId, userName, point)

    @Nested
    @DisplayName("사용자 저장 및 조회")
    inner class SaveAndFind {

        @Test
        fun `사용자를 저장하고 동일한 ID로 조회할 수 있다`() {
            // given
            val user = createUser()

            // when
            userRepository.save(user)
            val found = userRepository.findByUserId(user.userId)

            // then
            assertNotNull(found)
            assertEquals(user.userName, found?.userName)
            assertEquals(user.point, found?.point)
        }

        @Test
        fun `존재하지 않는 사용자 조회 시 null을 반환한다`() {
            val result = userRepository.findByUserId("non-existent-id")
            assertNull(result)
        }
    }

    @Nested
    @DisplayName("동일 ID 저장시 덮어쓰기 확인")
    inner class UpdateTest {

        @Test
        fun `동일한 userId로 저장 시 기존 값이 덮어써진다`() {
            // given
            val original = createUser(userId = "abc", userName = "철수", point = 1000)
            val updated = createUser(userId = "abc", userName = "영희", point = 2000)

            // when
            userRepository.save(original)
            userRepository.save(updated)
            val found = userRepository.findByUserId("abc")

            // then
            assertNotNull(found)
            assertEquals("영희", found?.userName)
            assertEquals(2000, found?.point)
        }
    }
}