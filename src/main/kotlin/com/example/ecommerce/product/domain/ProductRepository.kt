package com.example.ecommerce.product.domain

import java.security.Key

interface ProductRepository {
    fun save(product: Product): Product
    fun findByCode(code: String): Product?
    fun findByKey(productKey: Long) : Product?
    fun findAll(): List<Product>
    fun update(product: Product): Product
    fun delete(productCode: String)

    fun findAllOrderByPriceAsc(): List<Product>
    fun findAllOrderByPriceDesc(): List<Product>
}