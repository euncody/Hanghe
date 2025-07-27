package com.example.ecommerce.user

import com.example.ecommerce.user.domain.User
import com.example.ecommerce.user.infrastructure.entity.UserEntity

// 도메인 모델 ↔ 엔티티 간 변환을 담당하는 매퍼
object UserMapper {
    fun toDomain(entity: UserEntity): User =
        User(entity.userId!!, entity.name, entity.point)

    fun toEntity(domain: User): UserEntity =
        UserEntity(domain.userId, domain.name, domain.point)
}