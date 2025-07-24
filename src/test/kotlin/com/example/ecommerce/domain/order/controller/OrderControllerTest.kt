package com.example.ecommerce.domain.order.controller

import com.example.ecommerce.domain.order.model.request.OrderItemRequest
import com.example.ecommerce.domain.order.model.request.OrderRequest
import com.example.ecommerce.domain.order.model.response.OrderResponse
import com.example.ecommerce.domain.order.service.OrderService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(OrderController::class)
class OrderControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var orderService: OrderService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `주문을 등록한다`() {
        val request = OrderRequest(
            orderId = 1L,
            userId = 1L,
            items = listOf(OrderItemRequest(prodId = 1001L, orderQuantity = 2)),
            orderStatus = "주문 완료"
        )
        val response = OrderResponse(
            orderId = 1L,
            userId = 1L,
            items = request.items,
            totalPrice = 60000.0,
            orderStatus = "주문 완료"
        )

        given(orderService.addOrder(request)).willReturn(response)

        mockMvc.post("/orders") {
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
    fun `주문 내역을 조회한다`() {
        val responseList = listOf(
            OrderResponse(
                orderId = 1L,
                userId = 1L,
                items = listOf(OrderItemRequest(1001L, 2)),
                totalPrice = 60000.0,
                orderStatus = "주문 완료"
            )
        )

        given(orderService.getOrder(1L)).willReturn(responseList)

        mockMvc.get("/orders/1") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content {
                json(objectMapper.writeValueAsString(responseList))
            }
        }
    }

    @Test
    fun `주문 상태를 변경한다`() {
        val orderId = 1L
        val newStatus = "배송 중"

        mockMvc.post("/orders/$orderId/status") {
            contentType = MediaType.APPLICATION_JSON

            /* 수동으로 쌍따옴표 포함
            * -> 실제 요청 본문은 "\"배송 중\"" ← 이런 식으로 전송이 되어서
            * 컨트롤러가 문자열을 "배송 중" (쌍따옴표 포함)으로 받아서 응답에도 쌍따옴표가 표시됨
             */
            content = "\"배송 중\""
        }.andExpect {
            status { isOk() }
            content {
                string("주문 ID $orderId 의 상태가 '$newStatus' 로 변경되었습니다.")
            }
        }
    }
}
