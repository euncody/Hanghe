package com.example.ecommerce.global.lock

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.redisson.api.RedissonClient
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.EvaluationContext
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Component
import org.springframework.core.DefaultParameterNameDiscoverer
import java.util.concurrent.TimeUnit

@Aspect
@Component
class RedisLockAspect(
    private val redissonClient: org.redisson.api.RedissonClient
) {
    private val parser = SpelExpressionParser()
    private val nameDiscoverer = DefaultParameterNameDiscoverer()

    @Around("@annotation(redisLock)")
    @Throws(Throwable::class)
    fun around(joinPoint: ProceedingJoinPoint, redisLock: RedisLock): Any? {
        val key = resolveKey(redisLock.key, joinPoint)

        val lock = redissonClient.getLock("lock:$key")
        val waitTime = redisLock.waitTimeMs
        val leaseTime = redisLock.leaseTimeMs

        val locked = if (redisLock.failFast) {
            lock.tryLock(0, leaseTime, TimeUnit.MILLISECONDS)
        } else {
            lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS)
        }

        if (!locked) {
            throw IllegalStateException("락 획득 실패: $key")
        }

        return try {
            joinPoint.proceed()
        } finally {
            // 본 스레드가 보유한 락일 때만 해제 (재진입/중첩 대비)
            if (lock.isHeldByCurrentThread) {
                lock.unlock()
            }
        }
    }

    private fun resolveKey(keyExpression: String, pjp: ProceedingJoinPoint): String {
        val method = (pjp.signature as org.aspectj.lang.reflect.MethodSignature).method
        val paramNames = nameDiscoverer.getParameterNames(method)
        val args = pjp.args

        val context: EvaluationContext = StandardEvaluationContext()
        if (paramNames != null) {
            for (i in paramNames.indices) {
                context.setVariable(paramNames[i], args[i])
            }
        }
        val exp = parser.parseExpression(keyExpression)
        return exp.getValue(context, String::class.java) ?: keyExpression
    }
}