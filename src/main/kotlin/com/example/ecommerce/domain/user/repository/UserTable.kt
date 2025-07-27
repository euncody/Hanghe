package com.example.ecommerce.domain.user.repository

import com.example.ecommerce.domain.user.model.request.UserRequest
import org.springframework.stereotype.Repository

@Repository
class UserTable {
    private val userTable = HashMap<Long, UserRequest>()

    /*
    * User 생성
    * */
    fun addUser(userRequest : UserRequest) {
        userTable[userRequest.userId] = userRequest
    }

    /*
    * User 조회
    * */
    /* STUDY: User?
    *  User 객체를 반환할 수도 있고(null이 아닐 수도 있고), 반환하지 않을 수도 있다(null일 수 있다) */
    fun getUser(id: Long): UserRequest? {
        return userTable[id]
    }

    /*
    * 전체 User 조회
    * */
    fun getAllUsers(): List<UserRequest> {
        return userTable.values.toList()
    }


    /*
    * User 정보 업데이트
     */
    fun updateUser(user: UserRequest) {
        if (userTable.containsKey(user.userId)) {
            userTable[user.userId] = user
        } else {
            throw IllegalArgumentException("사용자를 찾을 수 없습니다.")
        }
    }


}