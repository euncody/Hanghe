package com.example.ecommerce.domain.product.model.request

import com.example.ecommerce.domain.product.model.repository.ProductTable
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProductTest {

    private lateinit var productTable: ProductTable

    @BeforeEach
    fun setUp() {
        productTable = ProductTable()
        val product = Product(1L, "상품1", 150.0)
        product.registerProduct("상품1", 150.0, "상품1 정보다", 10)
        productTable.addProduct(product)
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
    fun `제품을 등록한다`() {
        // given
        val product = Product(prodId = 2L, prodName = "상품2", prodPrice = 100.0)

        // when
        product.registerProduct(name = "상품2", price = 100.0, description = "상품2 정보다", stock = 20)
        productTable.addProduct(product)

        // then
        Assertions.assertEquals("상품2", product.prodName)
        Assertions.assertEquals(100.0, product.prodPrice)
        Assertions.assertEquals("상품2 정보다", product.prodDescription)
        Assertions.assertEquals(20, product.prodStock)

        Assertions.assertTrue(productTable.containsProduct(product.prodId))
        Assertions.assertEquals(product, productTable.getProduct(product.prodId))
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
    fun `제품의 재고를 수정한다`() {
        // given
        val product = productTable.getProduct(1L)

        // when
        product?.updateProductStock(15)

        // then
        Assertions.assertEquals(15, product?.prodStock)
    }

}