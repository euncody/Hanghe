package com.example.ecommerce.order

import com.example.ecommerce.order.domain.Order
import com.example.ecommerce.order.domain.OrderItem
import com.example.ecommerce.order.infrastructure.entity.OrderEntity
import com.example.ecommerce.order.infrastructure.entity.OrderItemEntity
import com.example.ecommerce.order.infrastructure.entity.OrderStatus
import com.example.ecommerce.user.UserMapper
import org.springframework.stereotype.Component

@Component
class OrderMapper(
    private val userMapper: UserMapper
) {

    fun toDomain(entity: OrderEntity): Order {
        return Order(
            orderKey = entity.orderKey,
            orderId = entity.orderId,
            user = userMapper.toDomain(entity.user),
            orderStatus = Order.OrderStatus.valueOf(entity.orderStatus.name),
            totalAmount = entity.totalAmount,
            orderItems = entity.orderItems.map { toOrderItemDomain(it) }
        )
    }

    fun toEntity(domain: Order): OrderEntity {
        val userEntity = userMapper.toEntity(domain.user)
        val orderEntity = OrderEntity(
            orderKey = domain.orderKey,
            orderId = domain.orderId,
            user = userEntity,
            orderStatus = OrderStatus.valueOf(domain.orderStatus.name),
            totalAmount = domain.totalAmount,
            orderItems = mutableListOf() // 빈 리스트로 먼저 초기화
        )

        val itemEntities = domain.orderItems.map {
            toOrderItemEntity(it).apply { order = orderEntity }
        }.toMutableList()

        orderEntity.orderItems = itemEntities
        return orderEntity
    }

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
            order = null!!, // 이후 toEntity에서 설정
            productKey = domain.productKey,
            quantity = domain.quantity,
            priceAtOrder = domain.priceAtOrder
        )
    }
}
