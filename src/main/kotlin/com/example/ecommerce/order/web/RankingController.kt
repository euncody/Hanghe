package com.example.ecommerce.order.web

import com.example.ecommerce.order.domain.PopularProductDto
import com.example.ecommerce.order.domain.ProductRankingService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

@RestController
@RequestMapping("/api/rank")
class RankingController(
    private val rankingService: ProductRankingService
) {

    @GetMapping("/popular")
    fun popular(
        @RequestParam(defaultValue = "10") topN: Int,

        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // ISO 8601 형식의 날짜 ->  예: 2023-10-01
        startDate: LocalDate?,

        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        endDate: LocalDate?
    ): List<PopularProductDto> {
        // (옵션) 입력 검증
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "startDate must be <= endDate")
        }
        return rankingService.findPopularProducts(topN, startDate, endDate)
    }
}