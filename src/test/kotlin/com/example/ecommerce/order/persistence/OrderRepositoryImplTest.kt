package com.example.ecommerce.order.persistence

import com.example.ecommerce.order.OrderMapper
import com.example.ecommerce.order.domain.Order
import com.example.ecommerce.order.domain.OrderItem
import com.example.ecommerce.order.infrastructure.entity.OrderEntity
import com.example.ecommerce.order.infrastructure.persistence.OrderJpaRepository
import com.example.ecommerce.order.infrastructure.persistence.OrderRepositoryImpl
import com.example.ecommerce.user.domain.User
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class OrderRepositoryImplTest {

    private val jpaRepository = mockk<OrderJpaRepository>()
    private val orderMapper = mockk<OrderMapper>()
    private val repository = OrderRepositoryImpl(jpaRepository, orderMapper)

    private val fakeUser = User(
        userKey = 1L,
        userId = "user123",
        userName = "홍길동",
        phone = "01012345678",
        email = "user@example.com",
        useState = "Y",
        hasCoupon = "N",
        registDate = LocalDateTime.now(),
        modiDate = null,
        deleteDate = null,
        point = 10000
    )

    private val fakeOrder = Order.registerOrder(
        orderId = "ORD123",
        user = fakeUser,
        orderItems = listOf(
            OrderItem.of("ITEM1", 1001, 1, 10000)
        )
    )

    private val fakeEntity = mockk<OrderEntity>()

    @Test
    fun `findByOrderId - 주문이 존재하면 반환`() {
        every { jpaRepository.findByOrderId("ORD123") } returns fakeEntity
        every { orderMapper.toDomain(fakeEntity) } returns fakeOrder

        val result = repository.findByOrderId("ORD123")

        assertEquals(fakeOrder, result)
    }

    @Test
    fun `findByOrderId - 주문이 없으면 null 반환`() {
        every { jpaRepository.findByOrderId("ORD999") } returns null

        val result = repository.findByOrderId("ORD999")

        assertNull(result)
    }

    @Test
    fun `save - 주문 저장 후 반환`() {
        every { orderMapper.toEntity(fakeOrder) } returns fakeEntity
        every { jpaRepository.save(fakeEntity) } returns fakeEntity
        every { orderMapper.toDomain(fakeEntity) } returns fakeOrder

        val result = repository.save(fakeOrder)

        assertEquals(fakeOrder, result)
    }

    @Test
    fun `delete - 주문 삭제`() {
        every { jpaRepository.findByOrderId("ORD123") } returns fakeEntity
        every { jpaRepository.delete(fakeEntity) } returns Unit

        repository.delete("ORD123")

        verify { jpaRepository.delete(fakeEntity) }
    }

    @Test
    fun `findAll - 전체 주문 조회`() {
        every { jpaRepository.findAll() } returns listOf(fakeEntity)
        every { orderMapper.toDomain(fakeEntity) } returns fakeOrder

        val result = repository.findAll()

        assertEquals(1, result.size)
        assertEquals(fakeOrder, result.first())
    }

    @Test
    fun `update - 주문 수정`() {
        val updated = fakeOrder.updateOrderStatus(Order.OrderStatus.CANCELLED)

        every { jpaRepository.findByOrderId(fakeOrder.orderId) } returns fakeEntity
        every { orderMapper.toEntity(updated) } returns fakeEntity
        every { fakeEntity.orderKey = any() } just Runs
        every { jpaRepository.save(fakeEntity) } returns fakeEntity
        every { orderMapper.toDomain(fakeEntity) } returns updated
        every { fakeEntity.orderKey } returns 1L

        val result = repository.update(updated)

        assertEquals(Order.OrderStatus.CANCELLED, result.orderStatus)
    }
}
