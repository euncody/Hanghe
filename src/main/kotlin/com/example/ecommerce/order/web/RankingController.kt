package com.example.ecommerce.order.web

import com.example.ecommerce.order.domain.PopularProductDto
import com.example.ecommerce.order.domain.ProductRankingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/rank")
class RankingController(
    private val rankingService: ProductRankingService
) {
    @GetMapping("/popular")
    fun popular(@RequestParam(defaultValue = "10") topN: Int): List<PopularProductDto> =
        rankingService.findPopularProducts(topN)
}