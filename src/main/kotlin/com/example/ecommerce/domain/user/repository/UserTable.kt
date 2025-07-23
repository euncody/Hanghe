package com.example.ecommerce.domain.user.repository

import com.example.ecommerce.domain.user.model.request.User

class UserTable {
    private val userTable = HashMap<Long, User>()

    /*
    * User 생성
    * */
    fun addUser(user : User) {
        userTable[user.userId] = user
    }


    /*
    * User 조회
    * */
    /* STUDY: User?
    *  User 객체를 반환할 수도 있고(null이 아닐 수도 있고), 반환하지 않을 수도 있다(null일 수 있다) */
    fun getUser(id: Long): User? {
        return userTable[id]
    }

    /*
    * 전체 User 조회
    * */
    fun getAllUsers(): List<User> {
        return userTable.values.toList()
    }




}