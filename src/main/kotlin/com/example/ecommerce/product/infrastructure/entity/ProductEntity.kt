package com.example.ecommerce.product.infrastructure.entity

import jakarta.persistence.*
import java.util.*


@Entity
@Table(
    name = "product",
    indexes = [Index(name = "product_code", columnList = "product_code", unique = true)]
)
class ProductEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_key")
    val productKey: Long? = null,

    @Column(name = "product_code", nullable = false, length = 36, unique = true)
    val productCode: String,

    @Column(name = "product_name", nullable = false, length = 100)
    var productName: String,

    @Column(name = "product_info", columnDefinition = "TEXT")
    var productInfo: String? = null,

    @Column(name = "price", nullable = false)
    var price: Int,

    @Column(name = "amount", nullable = false)
    var amount: Int
)