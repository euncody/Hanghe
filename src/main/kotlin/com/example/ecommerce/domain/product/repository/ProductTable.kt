package com.example.ecommerce.domain.product.repository

import com.example.ecommerce.domain.product.model.request.ProductRequest
import org.springframework.stereotype.Repository

@Repository
class ProductTable {
    private val productRequestTable = HashMap<Long, ProductRequest>()

    fun addProduct(productRequest: ProductRequest) {
        productRequestTable[productRequest.prodId] = productRequest
    }

    fun getProduct(id: Long): ProductRequest? {
        return productRequestTable[id]
    }

    fun getAllProducts(): List<ProductRequest> {
        return productRequestTable.values.toList()
    }

    fun containsProduct(id: Long): Boolean {
        return productRequestTable.containsKey(id)
    }

    fun updateProduct(productRequest: ProductRequest) {
        if (productRequestTable.containsKey(productRequest.prodId)) {
            productRequestTable[productRequest.prodId] = productRequest
        } else {
            throw IllegalArgumentException("상품을 찾을 수 없습니다.")
        }
    }
}