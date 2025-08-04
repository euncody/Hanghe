package com.example.ecommerce.product.service

import com.example.ecommerce.product.domain.Product
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ProductTest {

    @Test
    fun `재고를 정상적으로 차감한다`() {
        // given
        val product = Product(
            productKey = 1L,
            productCode = "P1234",
            productName = "테스트 상품",
            productInfo = "테스트용입니다",
            price = 10000,
            amount = 10
        )

        // when
        val updatedProduct = product.decreaseStock(3)

        // then
        assertEquals(7, updatedProduct.amount)
    }

    @Test
    fun `차감 수량이 0 이하면 예외 발생`() {
        val product = Product(
            productKey = 1L,
            productCode = "P1234",
            productName = "테스트 상품",
            productInfo = "테스트용입니다",
            price = 10000,
            amount = 10
        )

        val exception = assertThrows(IllegalArgumentException::class.java) {
            product.decreaseStock(0)
        }

        assertEquals("차감 수량은 0보다 커야 합니다.", exception.message)
    }

    @Test
    fun `재고보다 많은 수량을 차감할 경우 예외 발생`() {
        val product = Product(
            productKey = 1L,
            productCode = "P1234",
            productName = "테스트 상품",
            productInfo = "테스트용입니다",
            price = 10000,
            amount = 5
        )

        val exception = assertThrows(IllegalArgumentException::class.java) {
            product.decreaseStock(10)
        }

        assertEquals("재고가 부족합니다.", exception.message)
    }

    @Test
    fun `재고를 정상적으로 증가시킨다`() {
        val product = Product(
            productKey = 1L,
            productCode = "P1234",
            productName = "테스트 상품",
            productInfo = "테스트용입니다",
            price = 10000,
            amount = 5
        )

        val updatedProduct = product.increaseStock(10)

        assertEquals(15, updatedProduct.amount)
    }

    @Test
    fun `증가 수량이 0 이하면 예외 발생`() {
        val product = Product(
            productKey = 1L,
            productCode = "P1234",
            productName = "테스트 상품",
            productInfo = "테스트용입니다",
            price = 10000,
            amount = 5
        )

        val exception = assertThrows(IllegalArgumentException::class.java) {
            product.increaseStock(0)
        }

        assertEquals("추가 수량은 0보다 커야 합니다.", exception.message)
    }
}