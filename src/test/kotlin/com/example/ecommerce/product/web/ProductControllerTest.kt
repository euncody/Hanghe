package com.example.ecommerce.product.web

import com.example.ecommerce.product.domain.Product
import com.example.ecommerce.product.domain.ProductService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(ProductController::class)
class ProductControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var productService: ProductService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `상품을 등록한다`() {
        // given
        val request = ProductRequest(
            productCode = "p-001",
            productName = "마우스",
            productInfo = "무선마우스",
            price = 30000,
            amount = 10
        )

        val domainProduct = Product(
            productCode = "p-001",
            productName = "마우스",
            productInfo = "무선마우스",
            price = 30000,
            amount = 10
        )

        whenever(productService.createProduct(domainProduct)).thenReturn(domainProduct)

        // when & then
        mockMvc.post("/api/products") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.productCode").value("p-001")
            jsonPath("$.name").value("마우스")
        }
    }

    @Test
    fun `상품을 단건 조회한다`() {
        // given
        val productCode = "p-001"

        val product = Product(
            productCode = "p-001",
            productName = "마우스",
            productInfo = "무선마우스",
            price = 30000,
            amount = 10
        )

        whenever(productService.getProductByCode(productCode)).thenReturn(product)

        // when & then
        mockMvc.get("/api/products/{productCode}", productCode)
            .andExpect {
                status { isOk() }
                jsonPath("$.name").value("마우스")
                jsonPath("$.stock").value(10)
            }
    }

    @Test
    fun `상품 전체 조회`() {
        val products = listOf(
            Product(
                productCode = "p-001",
                productName = "마우스",
                productInfo = "무선마우스",
                price = 30000,
                amount = 10
            ),
            Product(
                productCode = "p-002",
                productName = "키보드",
                productInfo = "기계식 키보드",
                price = 40000,
                amount = 6
            )
        )

        whenever(productService.getAllProducts()).thenReturn(products)

        mockMvc.get("/api/products")
            .andExpect {
                status { isOk() }
                jsonPath("$.size()").value(2)
                jsonPath("$[0].name").value("마우스")
                jsonPath("$[1].name").value("키보드")
            }
    }

    @Test
    fun `상품 가격 오름차순 정렬 조회`() {
        val products = listOf(
            Product(
                productCode = "p-002",
                productName = "키보드",
                productInfo = "기계식 키보드",
                price = 40000,
                amount = 6
            ),
            Product(
                productCode = "p-001",
                productName = "마우스",
                productInfo = "무선마우스",
                price = 30000,
                amount = 10
            )
        )

        whenever(productService.getProductsOrderByPriceAsc()).thenReturn(products)

        mockMvc.get("/api/products/sorted/price/asc")
            .andExpect {
                status { isOk() }
                jsonPath("$.size()").value(2)
                jsonPath("$[0].name").value("마우스")
                jsonPath("$[1].name").value("키보드")
            }
    }

    @Test
    fun `상품 가격 내림차순 정렬 조회`() {
        val products = listOf(
            Product(
                productCode = "p-001",
                productName = "마우스",
                productInfo = "무선마우스",
                price = 30000,
                amount = 10
            ),
            Product(
                productCode = "p-002",
                productName = "키보드",
                productInfo = "기계식 키보드",
                price = 40000,
                amount = 6
            )
        )

        whenever(productService.getProductsOrderByPriceDesc()).thenReturn(products)

        mockMvc.get("/api/products/sorted/price/desc")
            .andExpect {
                status { isOk() }
                jsonPath("$.size()").value(2)
                jsonPath("$[0].name").value("키보드")
                jsonPath("$[1].name").value("마우스")
            }
    }
}
