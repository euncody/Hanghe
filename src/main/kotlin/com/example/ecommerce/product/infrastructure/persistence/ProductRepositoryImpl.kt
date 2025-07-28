package com.example.ecommerce.product.infrastructure.persistence

import com.example.ecommerce.product.ProductMapper
import com.example.ecommerce.product.domain.Product
import com.example.ecommerce.product.domain.ProductRepository
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl (
    private val productJpaRepository: ProductJpaRepository,
    private val productMapper: ProductMapper,
) : ProductRepository {

    override fun save(product: Product): Product {
        val entity = productMapper.toEntity(product)
        val saved = productJpaRepository.save(entity)
        return productMapper.toDomain(saved)
    }

    override fun findById(id: Long): Product? {
        return productJpaRepository.findById(id)
            .map { productMapper.toDomain(it) }
            .orElse(null)
    }

    override fun findByCode(code: String): Product? {
        return productJpaRepository.findByProductCode(code)
            ?.let { productMapper.toDomain(it) }
    }

    override fun findAll(): List<Product> {
        return productJpaRepository.findAll()
            .map { productMapper.toDomain(it) }
    }


}