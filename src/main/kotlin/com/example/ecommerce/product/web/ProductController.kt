package com.example.ecommerce.product.web

import com.example.ecommerce.product.domain.ProductService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService
) {

    // 상품 단건 조회
    @GetMapping("/{productCode}")
    fun getProduct(@PathVariable productCode: String): ProductResponse {
        val product = productService.getProductByCode(productCode)
        return ProductResponse.from(product)
    }

    // 전체 상품 조회
    @GetMapping
    fun getAllProducts(): List<ProductResponse> {
        val products = productService.getAllProducts()
        return products.map { ProductResponse.from(it) }
    }

    // 상품 등록
    @PostMapping
    fun createProduct(@RequestBody request: ProductRequest): ProductResponse {
        val product = request.toDomain()
        val saved = productService.createProduct(product)
        return ProductResponse.from(saved)
    }

    // 상품 수정
    @PutMapping("/{productCode}")
    fun updateProduct(
        @RequestBody request: ProductRequest
    ): ProductResponse {
        val updated = productService.updateProduct(request.toDomain())
        return ProductResponse.from(updated)
    }

    // 상품 삭제
    @DeleteMapping("/{productCode}")
    fun deleteProduct(@PathVariable productCode: String) {
        productService.deleteProduct(productCode)
    }
}