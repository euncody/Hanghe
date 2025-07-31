package com.example.ecommerce.order.web

import com.example.ecommerce.order.infrastructure.persistence.OrderJpaRepository
import com.example.ecommerce.order.infrastructure.entity.OrderEntity
import com.example.ecommerce.order.infrastructure.entity.OrderItemEntity
import com.example.ecommerce.user.infrastructure.entity.UserEntity
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import com.example.ecommerce.order.infrastructure.entity.OrderStatus
import com.example.ecommerce.product.infrastructure.entity.ProductEntity
import com.example.ecommerce.product.infrastructure.persistence.ProductJpaRepository
import com.example.ecommerce.user.infrastructure.persistence.UserJpaRepository


@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private lateinit var userJpaRepository: UserJpaRepository

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var orderJpaRepository: OrderJpaRepository


    @Autowired
    lateinit var productJpaRepository: ProductJpaRepository

    @Autowired
    lateinit var objectMapper: ObjectMapper

    private lateinit var savedUser: UserEntity
    private lateinit var savedProduct: ProductEntity
    private lateinit var savedOrder: OrderEntity


    @BeforeEach
    fun setUp() {
        savedUser = userJpaRepository.save(
            UserEntity(
                userId = "testuser",
                userName = "테스트 유저",
                email = "test@example.com",
                phone = "010-1234-5678",
                point = 1000
            )
        )

        savedProduct = productJpaRepository.save(
            ProductEntity(
                productCode = "prod123",
                productName = "테스트 상품",
                productInfo = "설명",
                price = 10000,
                amount = 5
            )
        )

        savedOrder = orderJpaRepository.save(
            OrderEntity(
                orderId = "order001",
                user = savedUser,
                totalAmount = 20000,
                orderStatus = OrderStatus.COMPLETED,
                orderItems = mutableListOf(
                    OrderItemEntity(
                        orderItemId = "item001",
                        productKey = savedProduct.productKey,
                        priceAtOrder = 10000,
                        quantity = 2,
                        order = OrderEntity(
                            orderId = "order001",
                            user = savedUser,
                            totalAmount = 20000,
                            orderStatus = OrderStatus.COMPLETED
                        )
                    )
                )
            )
        )
    }


    @Test
    fun `주문 단건 조회`() {
        mockMvc.perform(get("/api/orders/${savedOrder.orderId}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.orderId").value("order123"))
            .andExpect(jsonPath("$.orderStatus").value("COMPLETED"))
    }

    @Test
    fun `주문 목록 조회 - 사용자 기준`() {
        mockMvc.perform(get("/api/orders/user/${savedOrder.user.userId}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.size()").value(1))
    }

    @Test
    fun `주문 생성`() {
        val request = mapOf(
            "orderId" to "order999",
            "user" to mapOf("userId" to "user001"),
            "totalAmount" to 10000,
            "orderStatus" to "COMPLETED",
            "orderItems" to emptyList<Any>()
        )

        mockMvc.perform(
            post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.orderId").value("order999"))
    }

    @Test
    fun `주문 수정`() {
        val request = mapOf(
            "orderId" to savedOrder.orderId,
            "user" to mapOf("userId" to savedOrder.user.userId),
            "totalAmount" to 3000,
            "orderStatus" to "CANCELLED",
            "orderItems" to emptyList<Any>()
        )

        mockMvc.perform(
            put("/api/orders/${savedOrder.orderId}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.orderStatus").value("CANCELLED"))
    }

    @Test
    fun `주문 삭제`() {
        mockMvc.perform(delete("/api/orders/${savedOrder.orderId}"))
            .andExpect(status().isNoContent)
    }
}
