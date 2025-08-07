package com.example.ecommerce.order.service

import com.example.ecommerce.order.domain.Order
import com.example.ecommerce.order.domain.OrderItem
import com.example.ecommerce.order.domain.Order.OrderStatus
import com.example.ecommerce.order.domain.OrderRepository
import com.example.ecommerce.order.domain.OrderService
import com.example.ecommerce.product.domain.ProductService
import com.example.ecommerce.user.domain.User
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OrderServiceTest {

    private lateinit var orderRepository: OrderRepository
    private lateinit var orderService: OrderService
    private lateinit var productService: ProductService

    private lateinit var sampleUser: User
    private lateinit var sampleOrder: Order

    @BeforeEach
    fun setup() {
        orderRepository = mockk()
        orderService = OrderService(orderRepository, productService)

        sampleUser = User(
            userKey = 1L,
            userId = "user001",
            userName = "테스트유저",
            email = "test@example.com",
            phone = "010-1234-5678",
            point = 1000,
            hasCoupon = "N",
            useState = "Y"
        )

        sampleOrder = Order(
            orderKey = 1L,
            orderId = "order123",
            user = sampleUser,
            orderStatus = OrderStatus.COMPLETED,
            totalAmount = 30000,
            orderItems = listOf(
                OrderItem(
                    orderItemKey = 1L,
                    orderItemId = "item001",
                    productKey = 100L,
                    quantity = 2,
                    priceAtOrder = 15000
                )
            )
        )
    }

    @Test
    fun `createOrder - 주문 생성 성공`() {
        every { orderRepository.save(sampleOrder) } returns sampleOrder

        val result = orderService.createOrder(sampleOrder)

        assertEquals(sampleOrder.orderId, result.orderId)
        verify { orderRepository.save(sampleOrder) }
    }


    @Test
    fun `getOrdersByUserId - 유저의 전체 주문 목록 조회`() {
        every { orderRepository.findByUserId("user001") } returns listOf(sampleOrder)

        val result = orderService.getOrdersByUserId("user001")

        assertEquals(1, result.size)
        assertEquals("user001", result[0].user.userId)
    }

    @Test
    fun `updateOrder - 주문 상태 변경`() {
        val updatedOrder = sampleOrder.copy(orderStatus = OrderStatus.CANCELLED)

        every { orderRepository.update(updatedOrder) } returns updatedOrder

        val result = orderService.updateOrder(updatedOrder)

        assertEquals(OrderStatus.CANCELLED, result.orderStatus)
    }
}
