package com.example.ecommerce.domain.product.model.repository

import com.example.ecommerce.domain.product.model.request.Product

class ProductTable {
    private val productTable = HashMap<Long, Product>()

    fun addProduct(product: Product) {
        productTable[product.prodId] = product
    }

    fun getProduct(id: Long): Product? {
        return productTable[id]
    }

    fun getAllProducts(): List<Product> {
        return productTable.values.toList()
    }

    fun containsProduct(id: Long): Boolean {
        return productTable.containsKey(id)
    }
}