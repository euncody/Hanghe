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

    override fun findByCode(code: String): Product? {
        return productJpaRepository.findByProductCode(code)
            ?.let { productMapper.toDomain(it) }
    }

    override fun findAll(): List<Product> {
        return productJpaRepository.findAll()
            .map { productMapper.toDomain(it) }
    }

    override fun update(product: Product): Product {
        val entity = productJpaRepository.findByProductCode(product.productCode)
            ?: throw NoSuchElementException("수정할 상품이 존재하지 않습니다. productCode=${product.productCode}")

        entity.productName = product.productName
        entity.productInfo = product.productInfo
        entity.price = product.price
        entity.amount = product.amount

        return productMapper.toDomain(productJpaRepository.save(entity))
    }

    override fun delete(productCode: String) {
        val entity = productJpaRepository.findByProductCode(productCode)
            ?: throw NoSuchElementException("삭제할 상품이 존재하지 않습니다. productCode=$productCode")

        productJpaRepository.delete(entity)
    }

}