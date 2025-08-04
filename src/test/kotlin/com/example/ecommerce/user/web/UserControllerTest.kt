package com.example.ecommerce.user.web

import com.example.ecommerce.user.domain.User
import com.example.ecommerce.user.domain.UserService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UserController::class)
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    @Test
    fun `포인트 충전 API가 성공적으로 동작한다`() {
        // given
        val response = User(
            userId = "u-001",
            userName = "홍길동",
            email = "",
            point = 500
        )

        whenever(userService.chargeUserPoint("u-001", 500)).thenReturn(response)

        // when & then
        mockMvc.perform(
            post("/api/users/u-001/point")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """{
                    "userId": "u-001",
                    "point": 500
                }""".trimIndent()
                )
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.userId").value("u-001"))
            .andExpect(jsonPath("$.userName").value("홍길동"))
            .andExpect(jsonPath("$.point").value(500))
    }


    @Test
    fun `포인트 사용 API가 성공적으로 동작한다`() {
        val response = User(
            userId = "u-001",
            userName = "홍길동",
            email = "",
            point = 500
        )

        whenever(userService.useUserPoint("u-001", 500)).thenReturn(response)

        mockMvc.perform(
            post("/api/users/u-001/use-point")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"userId": "u-001", "point": 500}""")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.userId").value("u-001"))
            .andExpect(jsonPath("$.point").value(500))
    }

    @Test
    fun `userId가 URL과 요청 바디에서 다르면 400 오류가 발생한다`() {
        mockMvc.perform(
            post("/api/users/u-001/point")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"userId": "u-999", "point": 100}""") //  userId 불일치
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `음수 포인트를 충전하려 하면 400 오류가 발생한다`() {
        mockMvc.perform(
            post("/api/users/u-001/point")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"userId": "u-001", "point": -100}""")
        )
            .andExpect(status().isBadRequest)
    }

}
