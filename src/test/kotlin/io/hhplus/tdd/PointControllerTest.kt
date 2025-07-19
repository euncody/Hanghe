package io.hhplus.tdd

import io.hhplus.tdd.database.PointHistoryTable
import io.hhplus.tdd.database.UserPointTable
import io.hhplus.tdd.point.UserPoint
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import io.hhplus.tdd.point.PointController
import io.hhplus.tdd.point.PointHistory
import io.hhplus.tdd.point.TransactionType
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.BDDMockito.given
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch


@WebMvcTest(PointController::class) // PointController 만을 테스트하기 위한 설정
@AutoConfigureMockMvc// MockMvc 자동 설정
class PointControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc // MockMvc 를 사용하여 HTTP 요청을 테스트 - 가짜로 HTTP 요청을 보내는 도구

    @MockBean // UserPointTable 은 가짜로 만들어서, 원하는 값만 반환
    private lateinit var userPointTable : UserPointTable

    @MockBean
    private lateinit var pointHistoryTable: PointHistoryTable


    @BeforeEach // 각 테스트가 실행되기 전에 실행되는 메소드
    fun beforeEach() {
        /* 테스트가 실행될 때 Controller 가 userPointTable.selectById(1) 을 호출하면
         가짜 UserPoint 객체를 반환하도록 설정 */
        var userPoint = UserPoint(1, 100, System.currentTimeMillis())
        given(userPointTable.selectById(1)).willReturn(userPoint)
    }

    /*
    * given: 어떤 상황에서 (준비)
    * - 가짜 객체 만들기, 초기 데이터 설정
    *
    * when: 이런 행동을 하면 (실행)
    * - Controller 호출, 함수 호출
    *
    * then: 이런 결과가 나와야 한다 (검증)
    * -결과값 비교, 상태 코드 확인
    * */


    @Test
    fun `특정 유저의 포인트를 조회`() {
        mockMvc.perform(get("/point/1")) // 실제 서버를 띄우지 않고 가짜 HTTP 요청
            // 기대 결과: 상태 코드 + JSON 내용 확인
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.point").value(100))
    }


    @Test
    fun  `특정 유저의 포인트를 충전`() {
        /*
        * given : 이 메서드가 호출되면 이런 값을 돌려줘라
        * */
        // given: 충전할 유저 ID와 금액
        val userId = 1
        val chargeAmount = 50
        val expectedPoint = 150

        // 예상 결과 값 설정
        given(userPointTable.insertOrUpdate(1, 50)).willReturn(
            UserPoint(1, 150, System.currentTimeMillis())
        )

        // when: 충전 요청을 보냄
        val result = mockMvc.perform(
            patch("/point/${userId}/charge")
                .content(chargeAmount.toString())
                .contentType("application/json")
        )
            .andExpect(status().isOk)  // 상태 코드만 먼저 확인
            .andReturn()                // 결과 객체 반환


        // then: 상태 코드 + JSON 내용 확인
        val responseBody = result.response.contentAsString
        println(responseBody)  // 결과 확인용 출력

        // 예상 결과와 비교
        val expectedResult = """{"id":$userId,"point":$expectedPoint,"updateMillis":"""
        assertTrue(responseBody.contains("\"id\":$userId"))
        assertTrue(responseBody.contains("\"point\":$expectedPoint"))
    }


    @Test
    fun `특정 유저의 포인트를 사용`() {
        // given: 사용할 유저 ID와 금액
        val userId = 1
        val useAmount = 50
        val expectedPoint = 50

        // 예상 결과 값 설정
        given(userPointTable.insertOrUpdate(1, 50)).willReturn(
            UserPoint(1, 100, System.currentTimeMillis())
        )

        // when: 사용 요청을 보냄
        val result = mockMvc.perform(
            patch("/point/${userId}/use")
                .content(useAmount.toString())
                .contentType("application/json")
        )
            .andExpect(status().isOk)  // 상태 코드만 먼저 확인
            .andReturn()                // 결과 객체 반환
    }


    @Test
    fun `특정 유저의 포인트 충전이용 내역을 조회`() {
        // given: 조회할 유저 ID
        val userId: Long = 1

        // 예상 내역 준비
        given(pointHistoryTable.selectAllByUserId(userId))
            .willReturn(
                listOf(
                    PointHistory(0, userId.toLong(), TransactionType.CHARGE, 100, System.currentTimeMillis()),
                    PointHistory(1, userId.toLong(), TransactionType.USE, 50, System.currentTimeMillis())
                )
            )

        // when: 내역 조회 요청을 보냄
        mockMvc.perform(get("/point/${userId}/histories"))
            .andExpect(status().isOk)  // 상태 코드 확인
            .andExpect(jsonPath("$[0].userId").value(userId))  // 첫 번째 내역의 userId 확인
            .andExpect(jsonPath("$[0].amount").value(100))     // 첫 번째 내역의 amount 확인
            .andExpect(jsonPath("$[0].type").value("CHARGE")) // 첫 번째 내역의 type 확인

            .andExpect(jsonPath("$[1].userId").value(userId))  // 두 번째 내역의 userId 확인
            .andExpect(jsonPath("$[1].amount").value(50))     // 두 번째 내역의 amount 확인
            .andExpect(jsonPath("$[1].type").value("USE"))    // 두 번째 내역의 type 확인

    }

}


