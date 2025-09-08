package com.example.ecommerce.order.application

import com.example.ecommerce.order.domain.event.OrderPlacedEvent
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class OrderPlacedNotificationListener(
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onOrderPlaced(event: OrderPlacedEvent) {
        log.info("[Notif] AFTER_COMMIT 알림 발송 user={} order={} amount={}",
            event.userId, event.orderId, event.totalAmount)
    }
}