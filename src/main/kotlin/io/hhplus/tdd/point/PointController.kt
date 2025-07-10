package io.hhplus.tdd.point

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/point")
class PointController (
    private val userPointTable: UserPointTable // UserPointTable 의존성 주입
){

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    /**
     * TODO - 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}")
    fun point(
        @PathVariable id: Long,
    ): UserPoint {
        val userInfo = userPointTable.selectById(id)
        logger.info("유저 포인트 조회: id=$id, point=${userInfo.point}, updateMillis=${userInfo.updateMillis}")
        return userInfo
    }

    /**
     * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}/histories")
    fun history(
        @PathVariable id: Long,
    ): List<PointHistory> {
        // 유저 존재 확인
        val userExist = userPointTable.selectById(id)

        if (userExist.id.toInt() == 0) {
            logger.warn("유저가 존재하지 않습니다: id=$id")
            throw IllegalArgumentException("유저가 존재하지 않습니다: id=$id")
        }

        // 포인트 내역 조회
        val histories = pointHistoryTable.selectAllByUserId(id)
        logger.info("유저 포인트 내역 조회: id=$id, count=${histories.size}")
        return histories
    }

    /**
     * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/charge")
    fun charge(
        @PathVariable id: Long,
        @RequestBody amount: Long,
    ): UserPoint {
        // 유저 존재 확인
        val userExist = userPointTable.selectById(id)

        if (userExist.id.toInt() == 0) {
            logger.warn("유저가 존재하지 않습니다: id=$id")
            throw IllegalArgumentException("유저가 존재하지 않습니다: id=$id")

        } else {
            // 포인트 충전
            val updatedUserPoint = userPointTable.insertOrUpdate(id, amount)
            logger.info("유저 포인트 충전: id=$id, point=${updatedUserPoint.point}, updateMillis=${updatedUserPoint.updateMillis}")
            return updatedUserPoint
        }
    }

    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/use")
    fun use(
        @PathVariable id: Long,
        @RequestBody amount: Long,
    ): UserPoint {
        // 유저 존재 확인
        val userExist = userPointTable.selectById(id)

        if (userExist.id.toInt() == 0) {
            logger.warn("유저가 존재하지 않습니다: id=$id")
            throw IllegalArgumentException("유저가 존재하지 않습니다: id=$id")

        } else if (userExist.point < amount) {
            logger.warn("포인트가 부족합니다: id=$id, 현재 포인트=${userExist.point}, 사용하려는 포인트=$amount")
            throw IllegalArgumentException("포인트가 부족합니다: id=$id, 현재 포인트=${userExist.point}, 사용하려는 포인트=$amount")

        } else {
            // 포인트 사용
            val updatedUserPoint = userPointTable.insertOrUpdate(id, userExist.point - amount)
            logger.info("유저 포인트 사용: id=$id, point=${updatedUserPoint.point}, updateMillis=${updatedUserPoint.updateMillis}")
            return updatedUserPoint
        }
    }


}