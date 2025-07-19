package io.hhplus.tdd.ecommerce

import java.util.Date


data class User(
    val userId: Long,
    val userName: String,
    val phone: String,
    val email: String,
    val useState : Boolean = true,
    val hasCoupon: Boolean = false,
    val registDate : Date,
    val modiDate : Date,
    val deleteDate : Date
) {
    override fun toString(): String {
        return "User(userId=$userId, userName='$userName', phone='$phone', email='$email', useState=$useState, hasCoupon=$hasCoupon, registDate=$registDate, modiDate=$modiDate, deleteDate=$deleteDate)"
    }
}