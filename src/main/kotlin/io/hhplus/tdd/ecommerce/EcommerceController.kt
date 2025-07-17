package io.hhplus.tdd.ecommerce

import io.hhplus.tdd.point.UserPoint
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/ecommerce")
class EcommerceController {

    private val logger : Logger = LoggerFactory.getLogger(javaClass)

    /**
     * 특정 유저 조회 - 로그인 기능
     */
    @GetMapping("{id}")
    fun login(
        @PathVariable useId: Long,
    ): UserPoint {
        return UserPoint(0, 0, 0)
    }

    /**
     * 상위 상품 조회
     */
    @GetMapping("/bestList")
    fun bestList(): List<Product> {
        return emptyList()
    }

    /**
     *  상품 조회
     */
    @GetMapping("/productsList")
    fun productsList(): List<Product> {
        return emptyList()
    }

    /**
     * 주문
     */
    @PatchMapping("order")
    fun order(
        @PathVariable useId : Long,
        @RequestBody productIds: List<Long>
    ): Order {
        return Order(0, 0, emptyList())
    }


}