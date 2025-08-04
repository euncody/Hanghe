package com.example.ecommerce.product

import com.example.ecommerce.product.domain.Product
import com.example.ecommerce.product.infrastructure.entity.ProductEntity
import org.springframework.stereotype.Component

@Component
class ProductMapper {

    // ProductEntity → Product
    fun toDomain(entity: ProductEntity): Product {
        return Product(
            productKey = entity.productKey,
            productCode = entity.productCode,
            productName = entity.productName,
            productInfo = entity.productInfo,
            price = entity.price,
            amount = entity.amount
        )
    }

    // Product → ProductEntity
    fun toEntity(domain: Product): ProductEntity {
        return ProductEntity(
            productKey = domain.productKey,
            productCode = domain.productCode,
            productName = domain.productName,
            productInfo = domain.productInfo,
            price = domain.price,
            amount = domain.amount
        )
    }

}