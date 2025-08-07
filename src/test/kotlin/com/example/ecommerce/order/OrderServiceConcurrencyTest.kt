package com.example.ecommerce.order

import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.junit.jupiter.Testcontainers

import com.example.ecommerce.order.domain.Order
import com.example.ecommerce.order.domain.OrderItem
import com.example.ecommerce.order.domain.OrderRepository
import com.example.ecommerce.order.domain.OrderService
import com.example.ecommerce.product.domain.Product
import com.example.ecommerce.product.domain.ProductRepository
import com.example.ecommerce.product.domain.ProductService
import com.example.ecommerce.user.domain.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

@Testcontainers
@SpringBootTest
class OrderServiceConcurrencyTest {

    companion object {
        @Container
        val mysql = MySQLContainer<Nothing>("mysql:8.0.31")
            .apply {
                withDatabaseName("testdb")
                withUsername("test")
                withPassword("test")
                withInitScript("init-test-db.sql")
            }

        @JvmStatic
        @DynamicPropertySource
        fun props(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", mysql::getJdbcUrl)
            registry.add("spring.datasource.username", mysql::getUsername)
            registry.add("spring.datasource.password", mysql::getPassword)
        }
    }

    @Autowired lateinit var orderRepository: OrderRepository
    @Autowired lateinit var productRepository: ProductRepository
    @Autowired lateinit var productService: ProductService
    @Autowired lateinit var orderService: OrderService

    private val productKey = 1L
    private val initialStock = 100

    @BeforeEach
    fun setup() {
        // 재고 초기화: 100개짜리 상품 저장
        productRepository.save(
            Product(
                productKey   = productKey,
                productCode  = "P001",
                productName  = "동시성테스트상품",
                price        = 1000,
                amount       = initialStock,
                productInfo  = "Concurrent Test"
            )
        )
    }

    @Test
    fun `동시성 주문 요청 시 재고 초과되지 않아야 한다`() {
        val threadCount = 10
        val orderQuantityPerThread = 10
        val latch = CountDownLatch(1)
        val successCount = AtomicInteger(0)
        val failureCount = AtomicInteger(0)

        // 스레드풀 생성
        val executor = Executors.newFixedThreadPool(threadCount)

        repeat(threadCount) { idx ->
            executor.submit {
                try {
                    latch.await()

                    val user = User(
                        userId = "testUser$idx",
                        userName = "동시성테스트유저",
                        email = "test$idx@example.com",
                        point = initialStock * 1000   // 100 * 1000 = 100,000 포인트 :contentReference[oaicite:0]{index=0}
                    )

                    val order = Order(
                        orderId     = "order-$idx",
                        user        = user,
                        totalAmount = orderQuantityPerThread * 1000,
                        orderItems  = listOf(
                            OrderItem(
                                orderItemKey = 0L,                         // 아직 DB 저장 전이니 0L
                                orderItemId  = "order-${idx}-item",        // 고유한 ID
                                productKey   = productKey,
                                quantity     = orderQuantityPerThread,
                                priceAtOrder = 1000
                            )
                        )
                    )
                    orderService.createOrder(order)
                    successCount.incrementAndGet()
                } catch (ex: Exception) {
                    failureCount.incrementAndGet()
                }
            }
        }


        // 스레드 동시에 실행
        latch.countDown()
        executor.shutdown()
        val finished = executor.awaitTermination(30, TimeUnit.SECONDS)
        assertTrue(finished, "스레드가 시간 내 종료되지 않았습니다")

        // 성공 + 실패 합은 threadCount
        assertEquals(threadCount, successCount.get() + failureCount.get())

        // 최종 재고 확인: 100 - (성공한 스레드 수 * 10)
        val remaining = productService.getProductKey(productKey).amount
        assertEquals(
            initialStock - successCount.get() * orderQuantityPerThread,
            remaining,
            "재고가 예상값과 다릅니다"
        )
    }
}