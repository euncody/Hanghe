package com.example.ecommerce.user.infrastructure.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

// DB 테이블 매핑용
@Entity
@Table(name = "user")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_key")
    val userKey: Long? = null,

    @Column(name = "user_id", nullable = false)
    val userId: String,

    @Column(name = "user_name", nullable = false)
    val userName: String,

    @Column(name = "phone")
    val phone: String? = null,

    @Column(name = "email", nullable = false)
    val email: String,

    @Column(name = "use_state", columnDefinition = "ENUM('Y','N')", nullable = false)
    val useState: String = "Y",

    @Column(name = "has_coupon", columnDefinition = "ENUM('Y','N')", nullable = false)
    val hasCoupon: String = "N",

    @Column(name = "regist_date", updatable = false)
    val registDate: LocalDateTime = LocalDateTime.now(),

    @Column(name = "modi_date")
    val modiDate: LocalDateTime? = null,

    @Column(name = "delete_date")
    val deleteDate: LocalDateTime? = null,

    @Column(name = "point", nullable = false)
    val point: Int
){
    // JPA용 protected no-arg 생성자
    protected constructor() : this(
        null, "", "", null, "", "Y", "N", LocalDateTime.now(), null, null, 0
    )
}