package com.example.ecommerce.order

import com.example.ecommerce.order.domain.Order
import com.example.ecommerce.order.domain.OrderItem
import com.example.ecommerce.order.infrastructure.entity.OrderEntity
import com.example.ecommerce.order.infrastructure.entity.OrderItemEntity
import com.example.ecommerce.user.UserMapper
import org.springframework.stereotype.Component

@Component
class OrderMapper (
    private val userMapper: UserMapper // 추가
) {

    /**
     * OrderEntity → Order
     * UserEntity는 JPA로 로딩되므로 User로 변환해줌
     */
    fun toDomain(entity: OrderEntity): Order {
        val user = userMapper.toDomain(entity.user)
        return Order(
            orderKey = entity.orderKey,
            orderId = entity.orderId,
            user = user,
            orderStatus = Order.OrderStatus.valueOf(entity.orderStatus.name),
            totalAmount = entity.totalAmount,
            orderItems = entity.orderItems.map { toOrderItemDomain(it) }
        )
    }

    /**
     * Order → OrderEntity
     * UserEntity 전체를 직접 주입
     */
    fun toEntity(domain: Order): OrderEntity {
        val userEntity = userMapper.toEntity(domain.user)
        val orderItems = domain.orderItems.map { toOrderItemEntity(it) }.toMutableList()

        val orderEntity = OrderEntity(
            orderKey = domain.orderKey,
            orderId = domain.orderId,
            user = userEntity,
            orderStatus = com.example.ecommerce.order.infrastructure.entity.OrderStatus.valueOf(domain.orderStatus.name),
            totalAmount = domain.totalAmount,
            orderItems = orderItems
        )

        // 양방향 연관관계 설정
        orderItems.forEach { it.order = orderEntity }

        return orderEntity
    }


    // ─────────────────────────────────────────────
    // OrderItem 매핑 로직 (내부 통합)
    // ─────────────────────────────────────────────

    private fun toOrderItemDomain(entity: OrderItemEntity): OrderItem {
        return OrderItem(
            orderItemKey = entity.orderItemKey,
            orderItemId = entity.orderItemId,
            productKey = entity.productKey,
            quantity = entity.quantity,
            priceAtOrder = entity.priceAtOrder
        )
    }

    private fun toOrderItemEntity(domain: OrderItem): OrderItemEntity {
        return OrderItemEntity(
            orderItemKey = domain.orderItemKey,
            orderItemId = domain.orderItemId,
            order = null!!, // 후에 양방향 설정에서 세팅
            productKey = domain.productKey,
            quantity = domain.quantity,
            priceAtOrder = domain.priceAtOrder
        )
    }
}
