package com.example.ecommerce.order.application

import com.example.ecommerce.order.domain.event.OrderPlacedEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener


@Component
class OrderPlacedObservabilityListener(
) {
    private val log = LoggerFactory.getLogger(javaClass)


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun observe(event: OrderPlacedEvent) {
        log.info("[Obs] AFTER_COMMIT -> order={} amount={} items={}",
            event.orderId, event.totalAmount, event.productSummaries.size)
    }
}