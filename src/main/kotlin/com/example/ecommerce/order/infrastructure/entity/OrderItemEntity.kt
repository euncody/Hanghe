package com.example.ecommerce.order.infrastructure.entity

import jakarta.persistence.*

@Entity
@Table(name = "order_item")
class OrderItemEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val orderItemKey: Long = 0,

    @Column(nullable = false, unique = true)
    val orderItemId: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_key", nullable = false)
    val order: OrderEntity,

    @Column(nullable = false)
    val productKey: Long,

    @Column(nullable = false)
    val quantity: Int,

    @Column(nullable = false)
    val priceAtOrder: Int
)
