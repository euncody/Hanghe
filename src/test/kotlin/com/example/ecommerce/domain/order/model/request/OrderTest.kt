package com.example.ecommerce.domain.order.model.request

import com.example.ecommerce.domain.order.model.repository.OrderTable
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class OrderTest {

    private lateinit var orderTable: OrderTable

    @Test
    fun `주문을 등록하면 주문이 존재하고 상태는 주문 완료여야 한다`() {
        // given
        orderTable = OrderTable()
        val order = Order(
            orderId = 1L,
            id = 1L,
            prodId = 1L,
            orderQuantity = 2,
            totalPrice = 300.0,
            orderStatus = "주문 완료"
        )

        // when
        orderTable.addOrder(order)

        // then
        /*
        * STUDY: assertTrue(condition: Boolean, message: String? = null)
        * - condition: 참인지 검사할 Boolean 조건
        * - message: 조건이 false일 때 출력할 메시지
        * */
        assertTrue(orderTable.containsOrder(1L), "주문이 등록되지 않았습니다.")

        val registeredOrder = orderTable.getOrder(1L)
        assertNotNull(registeredOrder, "주문 조회 결과가 null입니다.")

        /*
        * STUDY: assertEquals(expected: Any?, actual: Any?, message: String? = null)
        * - expected: 기대값
        * - actual: 실제 실행 결과
        * - message: 실패 시 출력할 에러 메시지
        * */
        assertEquals("주문 완료", registeredOrder!!.orderStatus, "주문 상태가 '주문 완료'가 아닙니다.")
    }


    @Test
    fun `주문 정보를 조회한다`() {
        // given
        orderTable = OrderTable()
        val order = Order(orderId = 1L, id = 1L, prodId = 1L, orderQuantity = 2, totalPrice = 300.0,
                          orderStatus = "주문 완료")
        orderTable.addOrder(order)

        // when
        val retrievedOrder = orderTable.getOrder(1L)

        // then
        assert(retrievedOrder != null) { "주문 정보가 조회되지 않았습니다." }
        assert(retrievedOrder?.orderId == 1L) { "주문 ID가 일치하지 않습니다." }
    }

    @Test
    fun `주문 상태를 변경한다`() {
        // given
        orderTable = OrderTable()
        val order = Order(orderId = 1L, id = 1L, prodId = 1L, orderQuantity = 2, totalPrice = 300.0,
                          orderStatus = "주문 완료")
        orderTable.addOrder(order)

        // when
        order.updateOrderStatus("배송 중")

        // then
        assertEquals("배송 중", order.orderStatus, "주문 상태가 '배송 중'으로 변경되지 않았습니다.")
    }
}