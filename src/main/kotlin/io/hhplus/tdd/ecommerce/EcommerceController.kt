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
     * 특정 유저 조회 - 로그인
     */
    @GetMapping("{id}")
    fun login(
        @PathVariable userId: Long,
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
        @PathVariable userId : Long,
        @RequestBody productIds: List<Long>
    ): Order {
        return Order(0, 0, emptyList())
    }

    /**
     * 선착순 쿠폰 발급
     */
    @PostMapping("coupon")
    fun coupon(
        @PathVariable userId: Long,
        @RequestBody coupId: Long
    ): UserPoint {
        logger.info("쿠폰 발급 요청: userId=$userId, coupId=$coupId")
        return UserPoint(0, 0, 0)
    }


}