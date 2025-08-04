package com.example.ecommerce.product.persistence


import com.example.ecommerce.product.ProductMapper
import com.example.ecommerce.product.domain.Product
import com.example.ecommerce.product.infrastructure.entity.ProductEntity
import com.example.ecommerce.product.infrastructure.persistence.ProductJpaRepository
import com.example.ecommerce.product.infrastructure.persistence.ProductRepositoryImpl
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class ProductRepositoryImplTest {

    private lateinit var productRepository: ProductRepositoryImpl
    private val productJpaRepository: ProductJpaRepository = mockk()
    private val productMapper: ProductMapper = ProductMapper()

    private val sampleProduct = Product(
        productKey = 1L,
        productCode = "P-002",
        productName = "테스트 상품",
        productInfo = "상품 설명",
        price = 10000,
        amount = 50
    )

    private val sampleEntity = ProductEntity(
        productKey = 1L,
        productCode = "P-002",
        productName = "테스트 상품",
        productInfo = "상품 설명",
        price = 10000,
        amount = 50
    )

    @BeforeEach
    fun setUp() {
        productRepository = ProductRepositoryImpl(productJpaRepository, productMapper)
    }

    @Test
    fun `상품을 저장할 수 있다`() {
        every { productJpaRepository.save(any()) } returns sampleEntity

        val result = productRepository.save(sampleProduct)

        assertThat(result.productCode).isEqualTo("P-002")
        verify(exactly = 1) { productJpaRepository.save(any()) }
    }

    @Test
    fun `상품 코드를 통해 상품을 조회할 수 있다`() {
        every { productJpaRepository.findByProductCode("P-002") } returns sampleEntity

        val result = productRepository.findByCode("P-002")

        assertThat(result).isNotNull
        assertThat(result?.productName).isEqualTo("테스트 상품")
    }

    @Test
    fun `전체 상품 목록을 조회할 수 있다`() {
        every { productJpaRepository.findAll() } returns listOf(sampleEntity)

        val result = productRepository.findAll()

        assertThat(result).hasSize(1)
        assertThat(result[0].productCode).isEqualTo("P-002")
    }

    @Test
    fun `상품 정보를 수정할 수 있다`() {
        every { productJpaRepository.findByProductCode("P-002") } returns sampleEntity
        every { productJpaRepository.save(any()) } returns ProductEntity(
            productKey = 1L,
            productCode = "P-002",
            productName = "테스트 상품",
            productInfo = "상품 설명",
            price = 15000, // 수정된 값
            amount = 50
        )

        val updatedProduct = sampleProduct.copy(price = 15000)
        val result = productRepository.update(updatedProduct)

        assertThat(result.price).isEqualTo(15000)
    }

    @Test
    fun `존재하지 않는 상품은 수정 시 예외를 발생시킨다`() {
        every { productJpaRepository.findByProductCode("NONE") } returns null

        val invalidProduct = sampleProduct.copy(productCode = "NONE")

        val exception = org.junit.jupiter.api.assertThrows<NoSuchElementException> {
            productRepository.update(invalidProduct)
        }

        assertThat(exception.message).contains("수정할 상품이 존재하지 않습니다")
    }

    @Test
    fun `상품을 삭제할 수 있다`() {
        every { productJpaRepository.findByProductCode("P-002") } returns sampleEntity
        every { productJpaRepository.delete(any()) } just Runs

        productRepository.delete("P-002")

        verify { productJpaRepository.delete(any()) }
    }

    @Test
    fun `존재하지 않는 상품 삭제 시 예외를 발생시킨다`() {
        every { productJpaRepository.findByProductCode("NOT-EXIST") } returns null

        val exception = org.junit.jupiter.api.assertThrows<NoSuchElementException> {
            productRepository.delete("NOT-EXIST")
        }

        assertThat(exception.message).contains("삭제할 상품이 존재하지 않습니다")
    }
}