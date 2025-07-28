package com.example.ecommerce.product.domain

import org.springframework.stereotype.Service

@Service
class ProductService (
    private val productRepository: ProductRepository,
) {

}