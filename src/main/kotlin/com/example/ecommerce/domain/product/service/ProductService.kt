package com.example.ecommerce.domain.product.service

import com.example.ecommerce.domain.product.model.request.ProductRequest
import com.example.ecommerce.domain.product.model.response.ProductResponse
import com.example.ecommerce.domain.product.repository.ProductTable
import org.springframework.stereotype.Service

@Service
class ProductService(
     private val productTable: ProductTable
) {

    /*
     * 상품 등록
     */
    fun addProduct(productRequest: ProductRequest) {
        if (productTable.containsProduct(productRequest.prodId)) {
            throw IllegalArgumentException("이미 존재하는 상품입니다.")
        }
        productTable.addProduct(productRequest)
    }

    /*
     * 상품 조회
     */
    fun getProduct(prodId: Long): ProductResponse {
        val product = productTable.getProduct(prodId)
            ?: throw IllegalArgumentException("상품을 찾을 수 없습니다.")

        return ProductResponse(
            prodId = product.prodId,
            prodName = product.prodName,
            prodPrice = product.prodPrice,
            prodDescription = product.prodDescription,
            prodStock = product.prodStock
        )
    }

    /*
     * 전체 상품 조회
     */
    fun getAllProducts(): List<ProductResponse> {
        return productTable.getAllProducts().map { product ->
            ProductResponse(
                prodId = product.prodId,
                prodName = product.prodName,
                prodPrice = product.prodPrice,
                prodDescription = product.prodDescription,
                prodStock = product.prodStock
            )
        }
    }

    /*
     * 상품 수정
     */
    fun updateProduct(productRequest: ProductRequest) {
        if (!productTable.containsProduct(productRequest.prodId)) {
            throw IllegalArgumentException("상품을 찾을 수 없습니다.")
        }
        productTable.updateProduct(productRequest)
    }
}