package com.example.ecommerce.user

import com.example.ecommerce.user.domain.User
import com.example.ecommerce.user.infrastructure.entity.UserEntity
import org.springframework.stereotype.Component

// 도메인 모델 ↔ 엔티티 간 변환을 담당하는 매퍼

@Component
class UserMapper {

    // UserEntity → User
    fun toDomain(entity: UserEntity): User {
        return User(
            userKey = entity.userKey,
            userId = entity.userId,
            userName = entity.userName,
            phone = entity.phone,
            email = entity.email,
            useState = entity.useState,
            hasCoupon = entity.hasCoupon,
            registDate = entity.registDate,
            modiDate = entity.modiDate,
            deleteDate = entity.deleteDate,
            point = entity.point
        )
    }

    // User → UserEntity
    fun toEntity(domain: User): UserEntity {
        return UserEntity(
            userKey = domain.userKey,
            userId = domain.userId,
            userName = domain.userName,
            phone = domain.phone,
            email = domain.email,
            useState = domain.useState,
            hasCoupon = domain.hasCoupon,
            registDate = domain.registDate ?: java.time.LocalDateTime.now(),
            modiDate = domain.modiDate,
            deleteDate = domain.deleteDate,
            point = domain.point
        )
    }

}