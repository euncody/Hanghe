`@Cacheable`은 **Spring Cache 추상화**에서 제공하는 애너테이션으로, **메서드의 반환 값을 캐시에 저장**하고 **같은 요청이 다시 들어올 때는 DB나 서비스 로직을 실행하지 않고 캐시에서 바로 반환**하도록 해줍니다.

---

## **1. 기본 동작**

`@Cacheable`을 붙인 메서드는 다음 흐름으로 동작합니다:

1. 캐시에서 **키(key)** 로 데이터를 찾음
2. **데이터가 있으면** → 바로 캐시 값 반환 (DB 미조회)
3. **데이터가 없으면**

    * 메서드 본문(DB 조회 등) 실행
    * 결과를 캐시에 저장
    * 그 값을 반환

---

## **2. 간단한 예시**

```kotlin
@Service
class ProductRankingService(
    private val orderItemRankingRepository: OrderItemRankingRepository
) {

    @Cacheable(value = ["popularProducts"], key = "#topN")
    fun findPopularProducts(topN: Int): List<PopularProductDto> {
        println("DB에서 조회 중...") // 캐시가 없을 때만 실행됨
        return orderItemRankingRepository.findPopularProducts(topN)
    }
}
```

**설명**

* `value = ["popularProducts"]` → 캐시 이름(캐시 저장소의 영역 이름)
* `key = "#topN"` → `topN` 값을 캐시 키로 사용
* 첫 요청 시는 DB를 조회하지만, 이후 같은 `topN` 값으로 호출하면 캐시 값만 반환됨

---

## **3. 주요 옵션**

| 옵션             | 설명                     | 예시                                                    |
| -------------- | ---------------------- | ----------------------------------------------------- |
| `value`        | 캐시 이름                  | `value = ["popularProducts"]`                         |
| `key`          | 캐시 키                   | `key = "#topN"` 또는 `key = "#root.methodName + #topN"` |
| `condition`    | 특정 조건에서만 캐싱            | `condition = "#topN > 5"`                             |
| `unless`       | 결과가 특정 조건에 맞으면 캐싱하지 않음 | `unless = "#result.isEmpty()"`                        |
| `cacheManager` | 사용할 캐시 매니저 지정          | `cacheManager = "redisCacheManager"`                  |

---

## **4. Redis와 함께 사용하는 방법**

1. **설정 파일에 RedisCacheManager 등록**

```kotlin
@Configuration
@EnableCaching
class CacheConfig(
    private val redisConnectionFactory: RedisConnectionFactory
) {

    @Bean
    fun cacheManager(): RedisCacheManager {
        val config = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(10)) // TTL 10분 설정
        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(config)
            .build()
    }
}
```

2. **서비스에 `@Cacheable` 사용**

```kotlin
@Cacheable(value = ["popularProducts"], key = "#topN")
fun findPopularProducts(topN: Int): List<PopularProductDto> {
    return orderItemRankingRepository.findPopularProducts(topN)
}
```

이제 이 메서드를 호출하면 결과가 Redis에 저장되고, TTL(10분) 내에는 DB 조회 없이 Redis에서 바로 응답합니다.

---

## **5. 장점**

* 간단한 애너테이션만으로 캐싱 가능
* 로직 변경 없이 속도 개선 가능
* `TTL`, `조건부 캐싱`, `키 지정` 등 유연한 설정 가능

---

## **6. 주의할 점**

| 주의사항             | 이유/설명                                                         |
| ---------------- | ------------------------------------------------------------- |
| **TTL 설정**       | TTL을 너무 길게 두면 오래된 데이터가 반환될 수 있음                               |
| **데이터 변경 시 무효화** | DB 값이 바뀌어도 캐시는 자동 갱신되지 않음 → `@CacheEvict`나 `@CachePut`로 갱신 필요 |
| **키 충돌 주의**      | 동일한 캐시 이름과 키가 겹치지 않도록 명확하게 키를 지정해야 함                          |

---

## **7. 캐시 무효화 관련**

* **`@CacheEvict`** → 특정 키 삭제
* **`@CachePut`** → 캐시를 강제로 갱신

```kotlin
@CacheEvict(value = ["popularProducts"], key = "#topN")
fun clearCache(topN: Int) { }

@CachePut(value = ["popularProducts"], key = "#topN")
fun updateCache(topN: Int): List<PopularProductDto> {
    return orderItemRankingRepository.findPopularProducts(topN)
}
```

---

## **요약**

* `@Cacheable`은 **조회 성능을 높이기 위한 가장 간단한 캐싱 방법**
* Redis와 연결하면 대량 트래픽에서도 빠르고 안정적인 응답 가능
* TTL, 캐시 무효화 전략을 잘 설계해야 최신성을 유지할 수 있음

지금 구조에서는 `findPopularProducts()`에 `@Cacheable`을 붙이고, `CacheConfig`로 TTL만 적절히 지정하면 **DB → Redis → 빠른 조회** 흐름을 쉽게 구축할 수 있어요.
