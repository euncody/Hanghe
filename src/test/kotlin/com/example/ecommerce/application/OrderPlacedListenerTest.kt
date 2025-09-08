package com.example.ecommerce.application

import com.example.ecommerce.order.domain.event.OrderPlacedEvent
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.transaction.TransactionalTestExecutionListener
import org.springframework.test.context.transaction.TestTransaction
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@SpringBootTest
@TestExecutionListeners(
    listeners = [
        DependencyInjectionTestExecutionListener::class,
        TransactionalTestExecutionListener::class
    ]
)
class OrderPlacedListenerTest  @Autowired constructor(
    private val publisher: ApplicationEventPublisher,
    private val testProbe: TestProbe
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    @Transactional
    fun `AFTER_COMMIT 시점에 리스너가 실행된다`() {
        // given
        val evt = OrderPlacedEvent(
            orderId = "O-9999",
            userId = "U-XYZ",
            totalAmount = 12345,
            productSummaries = emptyList()
        )

        // when
        publisher.publishEvent(evt)

        assertTrue(testProbe.awaitNotCalled())

        TestTransaction.flagForCommit()
        TestTransaction.end()

        // then
        assertTrue(testProbe.awaitCalled(), "AFTER_COMMIT 리스너 호출이 감지되어야함")
        log.info("AFTER_COMMIT listener invocation confirmed.")
    }


    @org.springframework.stereotype.Component
    class TestProbe {
        private val called = CountDownLatch(1)
        private val notCalledCheck = CountDownLatch(1)

        @org.springframework.transaction.event.TransactionalEventListener(
            phase = org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT
        )
        fun on(event: OrderPlacedEvent) {
            called.countDown()
        }

        fun awaitNotCalled(timeoutMs: Long = 200): Boolean {
            return !called.await(timeoutMs, TimeUnit.MILLISECONDS)
        }

        fun awaitCalled(timeoutMs: Long = 5_000): Boolean {
            return called.await(timeoutMs, TimeUnit.MILLISECONDS)
        }
    }
}
