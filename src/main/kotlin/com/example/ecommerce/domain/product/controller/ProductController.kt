package com.example.ecommerce.domain.product.controller

import com.example.ecommerce.domain.product.model.request.ProductRequest
import com.example.ecommerce.domain.product.model.response.ProductResponse
import com.example.ecommerce.domain.product.service.ProductService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService
) {

    /*
     * 상품 등록
     */
     @PostMapping
     fun addProduct(@RequestBody productRequest: ProductRequest): String {
         productService.addProduct(productRequest)
         return "상품 ${productRequest.prodName} 이(가) 등록되었습니다."
     }

    /*
     * 상품 조회
     */
     @GetMapping("/{prodId}")
    fun getProduct(@PathVariable("prodId") id: Long): ProductResponse {
         return productService.getProduct(id)
     }

    /*
     * 전체 상품 조회
     */
     @GetMapping("/all")
     fun getAllProducts(): List<ProductResponse> {
         return productService.getAllProducts()
     }

    /*
    * 상품 수정
    * */
    @PostMapping("/modi")
    fun updateProduct(@RequestBody productRequest: ProductRequest): String {
        productService.updateProduct(productRequest) // Assuming addProduct can also handle updates
        return "상품 ${productRequest.prodName} 이(가) 업데이트되었습니다."
    }


}