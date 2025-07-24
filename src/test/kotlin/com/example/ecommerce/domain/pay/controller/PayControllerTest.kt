package com.example.ecommerce.domain.pay.controller

import com.example.ecommerce.domain.order.service.OrderService
import com.example.ecommerce.domain.pay.model.request.PayRequest
import com.example.ecommerce.domain.pay.model.response.PayResponse
import com.example.ecommerce.domain.pay.service.PayService
import com.example.ecommerce.domain.user.model.response.UserResponse
import com.example.ecommerce.domain.user.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content

@WebMvcTest(PayController::class)
class PayControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var payService: PayService

    @MockBean
    private lateinit var userService: UserService

    @MockBean
    private lateinit var orderService: OrderService

    @Test
    fun `결제 수단을 등록한다`() {
        val request = PayRequest(1L, 1L, 1L, 30000.0, "신용카드")
        val response = PayResponse(1L, 1L, 1L, 30000.0, "신용카드", "결제 대기 중")

        given(userService.getUserInfo(1L)).willReturn(UserResponse(1L, "user", 50000))
        given(payService.addPayment(request)).willReturn(response)

        mockMvc.post("/pays/reg") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content {
                json(objectMapper.writeValueAsString(response))
            }
        }
    }

    @Test
    fun `포인트가 충분하면 결제가 성공한다`() {
        val request = PayRequest(1L, 1L, 1L, 30000.0, "포인트")
        val response = PayResponse(1L, 1L, 1L, 30000.0, "포인트", "결제 완료")

        given(userService.getUserInfo(1L)).willReturn(UserResponse(1L, "user", 50000))
        given(orderService.getOrder(1L)).willReturn(emptyList())
        given(payService.processPayment(request)).willReturn(response)

        mockMvc.post("/pays/uses") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content {
                json(objectMapper.writeValueAsString(response))
            }
        }
    }

    @Test
    fun `포인트가 부족하면 예외가 발생한다`() {
        val request = PayRequest(1L, 1L, 1L, 60000.0, "포인트")

        given(userService.getUserInfo(1L)).willReturn(UserResponse(1L, "user", 50000))
        given(orderService.getOrder(1L)).willReturn(emptyList())

        mockMvc.post("/pays/uses") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { is4xxClientError() }
        }
    }
}
