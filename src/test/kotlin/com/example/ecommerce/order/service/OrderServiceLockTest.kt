package com.example.ecommerce.order.service

import com.example.ecommerce.order.domain.Order
import com.example.ecommerce.order.domain.OrderItem
import com.example.ecommerce.order.domain.OrderRepository
import com.example.ecommerce.order.domain.OrderService
import com.example.ecommerce.user.domain.User
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.assertEquals

@Testcontainers
@SpringBootTest
class OrderServiceLockTest {

    companion object {
        @Container
        val mysqlContainer = MySQLContainer<Nothing>("mysql:8.0.31").apply {
            withDatabaseName("testdb")
            withUsername("test")
            withPassword("test")
            withInitScript("init-test-db.sql")
        }

        @JvmStatic
        @DynamicPropertySource
        fun overrideProps(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl)
            registry.add("spring.datasource.username", mysqlContainer::getUsername)
            registry.add("spring.datasource.password", mysqlContainer::getPassword)
        }
    }

    @Autowired
    lateinit var orderService: OrderService

    @Autowired
    lateinit var orderRepository: OrderRepository

    @Test
    fun `비관적 락으로 주문 처리 테스트`() {
        // given
        val user =  User(
            userId = "testuser",
            userName = "Tester",
            email = "test@example.com",
            point = 100000
        )

        val orderItems = listOf(
            OrderItem.of("ORDER_ITEM_001", 1, 2, 10000),
            OrderItem.of("ORDER_ITEM_002", 2, 1, 15000)
        )

        val totalAmount = orderItems.sumOf { it.priceAtOrder * it.quantity }

        val order = Order(
            orderId = "ORDER_004",
            user = user,
            orderItems = orderItems,
            totalAmount = totalAmount
        )

        orderService.createOrder(order)

        // when
        val lockedOrder = orderRepository.findWithLock("ORDER_004")

        // then
        assertEquals("ORDER_004", lockedOrder.orderId)
    }


}