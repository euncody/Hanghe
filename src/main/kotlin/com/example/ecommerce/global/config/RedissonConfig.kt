package com.example.ecommerce.global.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedissonConfig(
    @Value("\${redis.host:localhost}") private val redisHost: String,
    @Value("\${redis.port:6379}") private val redisPort: Int
) {
    @Bean(destroyMethod = "shutdown")
    fun redissonClient(): RedissonClient {
        val config = Config()
        config.useSingleServer()
            .setAddress("redis://$redisHost:$redisPort")
            .setConnectionMinimumIdleSize(2)
            .setConnectionPoolSize(10)
            .setRetryAttempts(3)
            .setRetryInterval(1500)
            .setTimeout(3000)
        return Redisson.create(config)
    }
}