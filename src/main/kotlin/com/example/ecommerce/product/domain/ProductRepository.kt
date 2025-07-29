package com.example.ecommerce.product.domain

interface ProductRepository {
    fun save(product: Product): Product
    fun findByCode(code: String): Product?
    fun findAll(): List<Product>
    fun update(product: Product): Product
    fun delete(productCode: String)
}