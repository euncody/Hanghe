package com.example.ecommerce.product.infrastructure.persistence

import com.example.ecommerce.product.infrastructure.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductJpaRepository : JpaRepository<ProductEntity, Long> {

    fun findByProductCode(productCode: String): ProductEntity?

    fun findByProductKey(productKey: Long): ProductEntity?

    fun existsByProductCode(productCode: String): Boolean

    fun existsByProductKey(productKey: Long): Boolean

}