package com.example.ecommerce.order.infrastructure.entity

import jakarta.persistence.*

import com.example.ecommerce.user.infrastructure.entity.UserEntity

@Entity
@Table(name = "orders")
class OrderEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var orderKey: Long = 0,

    @Column(nullable = false, unique = true)
    val orderId: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key", nullable = false)
    val user: UserEntity,

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
