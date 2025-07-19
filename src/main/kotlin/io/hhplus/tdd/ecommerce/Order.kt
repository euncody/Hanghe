package io.hhplus.tdd.ecommerce

data class Order(
    val id: Long,
    val userId: Long,
    val productIds: List<Long>,
) {
    override fun toString(): String {
        return "Order(id=$id, userId=$userId, productIds=$productIds)"
    }
}