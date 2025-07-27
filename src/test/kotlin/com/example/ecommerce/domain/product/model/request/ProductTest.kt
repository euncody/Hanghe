package com.example.ecommerce.domain.product.model.request

import com.example.ecommerce.domain.product.repository.ProductTable
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class ProductTest {

    private lateinit var productTable: ProductTable

    @BeforeEach
    fun setUp() {
        productTable = ProductTable()
        val productRequest = ProductRequest(1L, "상품1", 150.0)
        productRequest.registerProduct("상품1", 150.0, "상품1 정보다", 10)
        productTable.addProduct(productRequest)
    }

    @Test
    fun `제품 목록을 조회한다`() {
        // given

        // when
        val productInfo = productTable.getAllProducts()

        // then
        Assertions.assertNotNull(productInfo)
    }

    @Test
    fun `prodId로 제품을 조회한다`() {
        // given
        // 이미 setUp에서 제품이 등록되어 있으므로 별도의 given 단계는 필요하지 않음

        // when
        val product = productTable.getProduct(1L)

        // then
        Assertions.assertNotNull(product)
        Assertions.assertEquals(1L, product?.prodId)
        Assertions.assertEquals("상품1", product?.prodName)
        Assertions.assertEquals(150.0, product?.prodPrice)
        Assertions.assertEquals("상품1 정보다", product?.prodDescription)
        Assertions.assertEquals(10, product?.prodStock)
    }

    @Test
    fun `다른 ID로 제품을 조회할 때 예외가 발생한다`() {
        val product = productTable.getProduct(1L)!!

        val exception = assertThrows(IllegalArgumentException::class.java) {
            product.getProductById(999L)
        }

        Assertions.assertEquals("제품 ID가 일치하지 않습니다.", exception.message)
    }


    @Test
    fun `제품을 등록한다`() {
        // given
        val productRequest = ProductRequest(prodId = 2L, prodName = "상품2", prodPrice = 100.0)

        // when
        productRequest.registerProduct(name = "상품2", price = 100.0, description = "상품2 정보다", stock = 20)
        productTable.addProduct(productRequest)

        // then
        Assertions.assertEquals("상품2", productRequest.prodName)
        Assertions.assertEquals(100.0, productRequest.prodPrice)
        Assertions.assertEquals("상품2 정보다", productRequest.prodDescription)
        Assertions.assertEquals(20, productRequest.prodStock)

        Assertions.assertTrue(productTable.containsProduct(productRequest.prodId))
        Assertions.assertEquals(productRequest, productTable.getProduct(productRequest.prodId))
    }

    @Test
    fun `이름이 비어 있거나 가격이 0 이하인 경우 제품 등록에 실패한다`() {
        val productRequest = ProductRequest(3L, "", -10.0)

        val exception = assertThrows(IllegalArgumentException::class.java) {
            productRequest.registerProduct("", -10.0, "잘못된 상품", 5)
        }

        Assertions.assertEquals("제품 이름은 비어있을 수 없고, 가격은 0보다 커야 합니다.", exception.message)
    }

    @Test
    fun `제품의 가격을 수정한다`() {
        // given
        val product = productTable.getProduct(1L)

        // when
        product?.updateProductPrice(200.0)

        // then
        Assertions.assertEquals(200.0, product?.prodPrice)
    }

    @Test
    fun `가격이 0 이하인 경우 제품 가격 수정에 실패한다`() {
        val product = productTable.getProduct(1L)!!

        val exception = assertThrows(IllegalArgumentException::class.java) {
            product.updateProductPrice(-100.0)
        }

        Assertions.assertEquals("가격은 0보다 큰 금액이어야 합니다.", exception.message)
    }


    @Test
    fun `제품의 재고를 수정한다`() {
        // given
        val product = productTable.getProduct(1L)

        // when
        product?.updateProductStock(15)

        // then
        Assertions.assertEquals(15, product?.prodStock)
    }

    @Test
    fun `재고 수량이 음수이면 예외가 발생한다`() {
        val product = productTable.getProduct(1L)!!

        val exception = assertThrows(IllegalArgumentException::class.java) {
            product.updateProductStock(-5)
        }

        Assertions.assertEquals("재고 수량은 음수일 수 없습니다.", exception.message)
    }


    @Test
    fun `제품 정보를 문자열로 반환한다`() {
        val product = productTable.getProduct(1L)!!
        val info = product.getProductInfo()

        Assertions.assertTrue(info.contains("제품 ID: 1"))
        Assertions.assertTrue(info.contains("이름: 상품1"))
    }


}