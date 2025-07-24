package com.example.ecommerce.domain.product.controller

import com.example.ecommerce.domain.product.model.request.ProductRequest
import com.example.ecommerce.domain.product.model.response.ProductResponse
import com.example.ecommerce.domain.product.service.ProductService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

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
        val request = ProductRequest(1L, "마우스", 30000.0, "무선마우스", 10)

        mockMvc.post("/products") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content { string("상품 마우스 이(가) 등록되었습니다.") }
        }
    }

    @Test
    fun `상품을 조회한다`() {
        val response = ProductResponse(1L, "마우스", 30000.0, "무선마우스", 10)

        given(productService.getProduct(1L)).willReturn(response)

        mockMvc.get("/products/1") {
            accept = MediaType.APPLICATION_JSON
            characterEncoding = "UTF-8" // 인코딩 설정
        }.andExpect {
            status { isOk() }
            content {
                json(objectMapper.writeValueAsString(response)) // JSON 구조 비교로 변경
            }
        }
    }


    @Test
    fun `전체 상품을 조회한다`() {
        val list = listOf(
            ProductResponse(1L, "마우스", 30000.0, "무선마우스", 10),
            ProductResponse(2L, "키보드", 40000.0, "기계식 키보드", 5)
        )

        given(productService.getAllProducts()).willReturn(list)

        mockMvc.get("/products/all")
            .andExpect {
                status { isOk() }
            }
    }
}
