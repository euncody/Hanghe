package com.example.ecommerce.global.lock

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RedisLock(
    val key: String,                 // 예: "user:point:#{#userId}"
    val waitTimeMs: Long = 2000,     // 락 대기 시간
    val leaseTimeMs: Long = 5000,    // 락 자동 해제 시간
    val failFast: Boolean = false    // 대기 없이 바로 실패할지
)