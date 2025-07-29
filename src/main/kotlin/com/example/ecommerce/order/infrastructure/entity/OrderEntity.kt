package com.example.ecommerce.order.infrastructure.entity

import jakarta.persistence.*

@Entity
@Table(name = "orders")
class OrderEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val orderKey: Long = 0,

    @Column(nullable = false, unique = true)
    val orderId: String,

    @Column(nullable = false)
    val userKey: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var orderStatus: OrderStatus = OrderStatus.COMPLETED,

    @Column(nullable = false)
    val totalAmount: Int,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    val orderItems: MutableList<OrderItemEntity> = mutableListOf()
)

enum class OrderStatus {
    COMPLETED, CANCELLED, DELIVERING
}
