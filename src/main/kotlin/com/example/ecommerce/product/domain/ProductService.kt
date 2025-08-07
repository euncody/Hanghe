package com.example.ecommerce.product.domain

import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository
) {

    fun createProduct(product: Product): Product {
        if (productRepository.findByCode(product.productCode) != null) {
            throw IllegalArgumentException("이미 존재하는 상품 코드입니다: ${product.productCode}")
        }
        return productRepository.save(product)
    }

    fun getProductByCode(productCode : String) : Product {
        return productRepository.findByCode(productCode)
            ?: throw NoSuchElementException("해당 상품이 존재하지 않습니다. productKey: $productCode")
    }

    fun getProductKey(productKey : Long) : Product {
        return productRepository.findByKey(productKey)
            ?: throw NoSuchElementException("해당 상품이 존재하지 않습니다. productKey: $productKey")
    }


    fun updateProduct(product: Product): Product {
        return productRepository.update(product)
    }

    fun getAllProducts(): List<Product> {
        return productRepository.findAll()
    }

    fun deleteProduct(productCode: String) {
        val product = productRepository.findByCode(productCode)
            ?: throw NoSuchElementException("해당 상품이 존재하지 않습니다. productKey: $productCode")
        productRepository.delete(productCode)
    }

    fun getProductsOrderByPriceAsc(): List<Product> {
        return productRepository.findAllOrderByPriceAsc()
    }

    fun getProductsOrderByPriceDesc(): List<Product> {
        return productRepository.findAllOrderByPriceDesc()
    }


}