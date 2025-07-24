package com.example.ecommerce.domain.user.controller

import com.example.ecommerce.domain.user.model.request.UserRequest
import com.example.ecommerce.domain.user.repository.UserTable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.hamcrest.Matchers.containsString


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userTable: UserTable

    @BeforeEach
    fun setup() {
        userTable.addUser(UserRequest(userId = 1L, name = "사용자1", point = 500))
    }

    @Test
    fun `사용자 정보를 조회할 수 있다`() {
        mockMvc.get("/users/1")
            .andExpect {
                status { isOk() }
                content { string(containsString("사용자1")) }
            }
    }

    @Test
    fun `사용자 포인트를 충전할 수 있다`() {
        mockMvc.get("/users/1/charge?amount=100")
            .andExpect {
                status { isOk() }
                content { string(containsString("포인트를 100 만큼 충전")) }
            }
    }

    @Test
    fun `사용자 포인트를 사용할 수 있다`() {
        mockMvc.get("/users/1/use?amount=200")
            .andExpect {
                status { isOk() }
                content { string(containsString("포인트를 200 만큼 사용")) }
            }
    }

    @Test
    fun `사용자 포인트가 부족하면 예외가 발생한다`() {
        mockMvc.get("/users/1/use?amount=1000")
            .andExpect {
                status { is4xxClientError() }
            }
    }

    @Test
    fun `존재하지 않는 사용자 조회 시 예외가 발생한다`() {
        mockMvc.get("/users/999")
            .andExpect {
                status { is4xxClientError() }
            }
    }
}
