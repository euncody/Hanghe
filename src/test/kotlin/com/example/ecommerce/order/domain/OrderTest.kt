package com.example.ecommerce.order.domain

import com.example.ecommerce.user.domain.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class OrderTest {

    private fun createFakeUser(): User {
        return User(
            userKey = 1L,
            userId = "user123",
            userName = "홍길동",
            phone = "01012345678",
            email = "user@example.com",
            useState = "Y",
            hasCoupon = "N",
            registDate = LocalDateTime.now(),
            modiDate = null,
            deleteDate = null,
            point = 10000
        )
    }

    @Test
    fun `주문 생성 성공`() {
        // given
        val user = createFakeUser()
        val orderItems = listOf(
            OrderItem.of("item1", 1001, 2, 3000),
            OrderItem.of("item2", 1002, 1, 5000)
        )

        // when
        val order = Order.registerOrder("ORD123", user, orderItems)

        // then
        assertEquals("ORD123", order.orderId)
        assertEquals(user, order.user)
        assertEquals(2, order.orderItems.size)
        assertEquals(11000, order.totalAmount)
        assertEquals(Order.OrderStatus.COMPLETED, order.orderStatus)
    }

    @Test
    fun `주문 상태 변경`() {
        val order = Order.registerOrder("ORD124", createFakeUser(), listOf(
            OrderItem.of("item1", 1001, 1, 10000)
        ))

        val updated = order.updateOrderStatus(Order.OrderStatus.DELIVERING)

        assertEquals(Order.OrderStatus.DELIVERING, updated.orderStatus)
    }

    @Test
    fun `주문 정보 문자열 반환`() {
        val user = createFakeUser()
        val order = Order.registerOrder("ORD125", user, listOf(
            OrderItem.of("item1", 1001, 1, 10000)
        ))

        val info = order.getOrderInfo()

        assertTrue(info.contains("ORD125"))
        assertTrue(info.contains(user.userKey.toString()))
        assertTrue(info.contains("10000"))
    }

    @Test
    fun `빈 주문 ID 예외 발생`() {
        val user = createFakeUser()

        val exception = assertThrows(IllegalArgumentException::class.java) {
            Order.registerOrder("", user, listOf(
                OrderItem.of("item1", 1001, 1, 3000)
            ))
        }

        assertEquals("주문 ID는 비어 있을 수 없습니다.", exception.message)
    }

    @Test
    fun `주문 항목 없음 예외 발생`() {
        val user = createFakeUser()

        val exception = assertThrows(IllegalArgumentException::class.java) {
            Order.registerOrder("ORD126", user, emptyList())
        }

        assertEquals("주문 항목은 1개 이상이어야 합니다.", exception.message)
    }
}
