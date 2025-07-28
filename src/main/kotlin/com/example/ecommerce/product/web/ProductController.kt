package com.example.ecommerce.product.web

import com.example.ecommerce.product.domain.ProductService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/products")
class ProductController (
    private val productService: ProductService
) {

}