package com.example.ecommerce.order.web

import com.example.ecommerce.order.domain.PopularProductDto
import com.example.ecommerce.order.domain.ProductRankingService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.time.LocalDate

@WebMvcTest(RankingController::class)
class RankingControllerTest(
    @Autowired val mockMvc: MockMvc
) {

    @MockBean
    private lateinit var productRankingService: ProductRankingService

    @Test
    fun `상위 10개 인기상품 조회 - 날짜 조건 없이`() {
        // given
        val mockResponse = listOf(
            PopularProductDto(1L, "P001", "상품1", 50, 500000),
            PopularProductDto(2L, "P002", "상품2", 30, 300000)
        )
        given(productRankingService.findPopularProducts(10, null, null))
            .willReturn(mockResponse)

        // when & then
        mockMvc.get("/api/rank/popular?topN=10")
            .andExpect {
                status { isOk() }
                jsonPath("$[0].productCode") { value("P001") }
                jsonPath("$[0].totalQty") { value(50) }
                jsonPath("$[1].productCode") { value("P002") }
                jsonPath("$[1].totalRev") { value(300000) }
            }
    }

    @Test
    fun `상위 5개 인기상품 조회 - 날짜 조건 포함`() {
        // given
        val start = LocalDate.of(2025, 8, 1)
        val end = LocalDate.of(2025, 8, 19)

        val mockResponse = listOf(
            PopularProductDto(3L, "P003", "상품3", 20, 200000)
        )
        given(productRankingService.findPopularProducts(5, start, end))
            .willReturn(mockResponse)

        // when & then
        mockMvc.get("/api/rank/popular?topN=5&startDate=2025-08-01&endDate=20250819")
            .andExpect {
                status { isOk() }
                jsonPath("$[0].productCode") { value("P003") }
                jsonPath("$[0].totalQty") { value(20) }
            }
    }

    @Test
    fun `잘못된 날짜 포맷`() {
        mockMvc.get("/api/rank/popular?startDate=2025-99-99")
            .andExpect {
                status { isBadRequest() }
                jsonPath("$.message") { value("Invalid date format: '2025-99-99' (use yyyy-MM-dd or yyyyMMdd)") }
            }
    }

    @Test
    fun `날짜 범위가 잘못된 경우`() {
        mockMvc.get("/api/rank/popular?startDate=2025-08-20&endDate=2025-08-01")
            .andExpect {
                status { isBadRequest() }
                jsonPath("$.message") { value("startDate must be <= endDate") }
            }
    }
}
