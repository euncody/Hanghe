package com.example.ecommerce.order.application

import com.example.ecommerce.order.domain.event.OrderPlacedEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener


@Component
class OrderPlacedDispatchListener(
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun dispatch(event: OrderPlacedEvent) {
        log.info("[Dispatch] AFTER_COMMIT 주문 전달 완료 order={} items={}",
            event.orderId, event.productSummaries.size)
    }
}