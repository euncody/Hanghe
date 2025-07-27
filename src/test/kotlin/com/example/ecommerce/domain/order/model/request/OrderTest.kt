package com.example.ecommerce.domain.order.model.request

import com.example.ecommerce.domain.order.model.request.OrderItemRequest
import com.example.ecommerce.domain.order.model.request.OrderRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class OrderTest {
    @Test
    fun `정상적인 주문 등록 테스트`() {
        val order = OrderRequest(orderId = 1L, userId = 1L, orderStatus = "주문 완료")
        order.registerOrder(userId = 1L, prodId = 1001L, quantity = 2)

        assertEquals(1L, order.userId)
        assertEquals(1, order.items.size)
        assertEquals("주문 완료", order.orderStatus)
        assertEquals(1001L, order.items.first().prodId)
    }

    @Test
    fun `잘못된 값으로 주문 등록 시 예외 발생`() {
        val order = OrderRequest(orderId = 2L, userId = 0L, orderStatus = "주문 완료")

        val exception = assertThrows<IllegalArgumentException> {
            order.registerOrder(userId = -1L, prodId = 0L, quantity = -5)
        }

        assertEquals("사용자 ID, 제품 ID, 수량은 모두 0보다 큰 값이어야 합니다.", exception.message)
    }

    @Test
    fun `주문 상태 변경 테스트`() {
        val order = OrderRequest(orderId = 3L, userId = 1L, orderStatus = "주문 완료")
        order.updateOrderStatus("배송 중")

        assertEquals("배송 중", order.orderStatus)
    }

    @Test
    fun `유효하지 않은 주문 상태 변경 시 예외 발생`() {
        val order = OrderRequest(orderId = 4L, userId = 1L, orderStatus = "주문 완료")

        val exception = assertThrows<IllegalArgumentException> {
            order.updateOrderStatus("환불 대기")
        }

        assertEquals("유효하지 않은 주문 상태입니다. 가능한 상태: [주문 완료, 주문 취소, 배송 중]", exception.message)
    }

    @Test
    fun `주문 정보 조회 테스트`() {
        val order = OrderRequest(orderId = 5L, userId = 10L, orderStatus = "주문 완료")
        order.registerOrder(userId = 10L, prodId = 1002L, quantity = 1)

        val info = order.getOrderInfo(10L)
        assertTrue(info.contains("주문 ID: 5"))
        assertTrue(info.contains("사용자 ID: 10"))
        assertTrue(info.contains("제품 목록: [1002]"))
    }

    @Test
    fun `다른 사용자로 주문 정보 조회 시 예외 발생`() {
        val order = OrderRequest(orderId = 6L, userId = 10L, orderStatus = "주문 완료")

        val exception = assertThrows<IllegalArgumentException> {
            order.getOrderInfo(99L)
        }

        assertEquals("사용자 ID가 일치하지 않습니다.", exception.message)
    }


}