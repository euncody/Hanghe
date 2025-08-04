package com.example.ecommerce.order.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class OrderItemTest {

    @Test
    fun `정상적인 주문 아이템 생성`() {
        val orderItem = OrderItem.of("ITEM123", 1001, 2, 5000)

        assertEquals("ITEM123", orderItem.orderItemId)
        assertEquals(1001, orderItem.productKey)
        assertEquals(2, orderItem.quantity)
        assertEquals(5000, orderItem.priceAtOrder)
    }

    @Test
    fun `수량이 0 이하일 경우 예외 발생`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            OrderItem.of("ITEM124", 1001, 0, 5000)
        }

        assertEquals("수량은 1 이상이어야 합니다.", exception.message)
    }

    @Test
    fun `가격이 음수일 경우 예외 발생`() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
            OrderItem.of("ITEM125", 1001, 1, -1000)
        }

        assertEquals("가격은 0 이상이어야 합니다.", exception.message)
    }

    @Test
    fun `총 금액 계산`() {
        val orderItem = OrderItem.of("ITEM126", 1001, 3, 4000)

        val totalPrice = orderItem.quantity * orderItem.priceAtOrder

        assertEquals(12000, totalPrice)
    }
}
