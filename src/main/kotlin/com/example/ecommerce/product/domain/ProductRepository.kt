package com.example.ecommerce.product.domain

interface ProductRepository {
    fun save(product: Product): Product
    fun findById(id: Long): Product?
    fun findByCode(code: String): Product?
    fun findAll(): List<Product>
}