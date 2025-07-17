package io.hhplus.tdd.ecommerce

import java.util.Date


data class Coupon(
    val coupId: Long,
    val coupName: String,
    val coupDesc: String,
    val discountAmount : Long,
    val totalCount : Int,
    val registDate : Date,
    val modiDate : Date,
    val deleteDate : Date
) {
    override fun toString(): String {
        return "Coupon(coupId=$coupId, coupName='$coupName', coupDesc='$coupDesc', discountAmount=$discountAmount, totalCount=$totalCount, registDate=$registDate, modiDate=$modiDate, deleteDate=$deleteDate)"
    }
}