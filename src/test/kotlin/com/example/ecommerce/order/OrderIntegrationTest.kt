package com.example.ecommerce.order


import com.example.ecommerce.order.web.OrderItemRequest
import com.example.ecommerce.order.web.OrderRequest
import com.example.ecommerce.product.domain.ProductRepository
import com.example.ecommerce.user.domain.UserRepository
import com.example.ecommerce.user.web.UserRequest
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.assertEquals

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderIntegrationTest @Autowired constructor(
    val mockMvc: MockMvc,
    val userRepository: UserRepository,
    val productRepository: ProductRepository
) {

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

    @Test
    @Transactional
    fun `사용자가 충분한 잔액을 가지고 있을 때 주문 성공하고 잔액과 재고가 차감된다`() {
        val request = OrderRequest(
            orderId = "ORDER_TEST_001",
            user = UserRequest(
                userId = "testuser",
                userName = "Tester",
                phone = "010-1234-5678",
                email = "test@example.com"
            ),
            orderStatus = "COMPLETED",
            totalAmount = 20000,
            orderItems = listOf(
                OrderItemRequest(
                    orderItemId = "ITEM_TEST_001",
                    productKey = 1L,  // 실제 DB에 있는 product_key=1 사용
                    quantity = 2,
                    priceAtOrder = 10000
                )
            )
        )

        mockMvc.post("/api/orders") {
            contentType = MediaType.APPLICATION_JSON
            content = asJsonString(request)
        }.andExpect {
            status { isOk() }
        }

        val user = userRepository.findByUserId("testuser")
            ?: throw IllegalStateException("User not found")

        val product = productRepository.findByKey(1L)
            ?: throw IllegalStateException("Product not found")


        assertEquals(80_000, user.point)
        assertEquals(8, product.amount)
    }

    private fun asJsonString(obj: Any): String =
        com.fasterxml.jackson.module.kotlin.jacksonObjectMapper().writeValueAsString(obj)
}
