“@Cacheable이 하는 일”을 **개념부터 내부 동작, 키 생성, 히트/미스 판단, 주의점**까지 제대로 풀어서 설명할게요.

---

# 1) 왜 프록시(Proxy)로 가로채나?

스프링은 `@Cacheable`이 붙은 **메서드 호출을 AOP 프록시**로 감싸요.
그래야 **“메서드 실행 전”에 캐시를 먼저 확인**하고, **히트면 메서드 본문을 아예 실행하지 않도록** 차단할 수 있거든요.

* 호출 흐름(간단화)

    1. 호출자가 `service.findPopularProducts(10)` 호출
    2. 실제 빈이 아니라 **프록시**가 호출을 받음
    3. 프록시가 **캐시 키 생성 → 캐시 조회**
    4. 히트면 바로 캐시값 반환 (본문 실행 X)
    5. 미스면 **실제 메서드 실행 → 결과를 캐시에 저장 → 결과 반환**

> **Self-invocation 주의**
> 같은 클래스 안에서 `this.someCacheable()` 처럼 **자기 자신 메서드를 직접 호출**하면 프록시를 안 거칩니다 → 캐싱이 적용되지 않아요.
> 해결: (a) 메서드를 **다른 빈**으로 분리, (b) **self 주입**(프록시 타입으로 자기 자신을 주입받아 호출), (c) `AopContext` 사용(고급).

---

# 2) 캐시 키는 어떻게 만들어지나?

스프링은 캐시 키가 있어야 “같은 요청인지”를 판단합니다.

* **기본값**: 파라미터 조합을 `SimpleKey`로 생성

    * 인자 0개 → `SimpleKey.EMPTY`
    * 1개 → 그 값
    * N개 → `(arg1,arg2,...)` 형태의 합성 키(동등성/해시 구현돼 있음)
* **SpEL로 직접 지정**: 가장 많이 쓰는 방법

  ```kotlin
  @Cacheable(cacheNames=["popularProducts"], key="#topN + ':' + (#startDate?:'ALL') + ':' + (#endDate?:'ALL')")
  ```

    * `#paramName` 로 인자 참조
    * `#root.methodName`, `#root.targetClass` 등 메타도 가능
    * `?:`(Elvis)로 널 처리도 가능
* **여러 캐시 이름**: `cacheNames=["A","B"]` 지정 시

    * 조회는 **순서대로** 캐시를 검사하다가 **첫 히트**면 반환
    * 미스면 **모든 캐시**에 저장

> **키 충돌 방지**: 캐시 이름과 키 설계를 명확히. 필요하면 해시(MD5 등)로 압축.

---

# 3) Hit / Miss의 진짜 의미

* **Hit(캐시 있음)**: 프록시가 메서드 본문을 **실행하지 않음** → DB도 안 감
* **Miss(캐시 없음)**: 메서드 **실행 후 결과 저장**
* 결과 저장 시점: **메서드가 정상 반환한 후**

    * 예외를 던지면 저장 안 함
    * 결과가 `null`인 경우는 **기본 정책**에 따라 다름

        * RedisCache 기본 설정에선 “null 캐시 금지”가 흔합니다(설정으로 허용 가능)

---

# 4) TTL은 누가 정하나? (@Cacheable 자체가 아님)

* `@Cacheable`은 “**무엇을 캐시할지**”만 결정
* **TTL(유효기간)** 은 **CacheManager(예: RedisCacheManager) 설정**에서 정해요.

  ```kotlin
  val config = RedisCacheConfiguration.defaultCacheConfig()
      .entryTtl(Duration.ofMinutes(10)) // 기본 TTL 10분
  RedisCacheManager.builder(factory).cacheDefaults(config).build()
  ```
* 캐시 이름별 서로 다른 TTL을 줄 수도 있어요(핫한 데이터는 5분, 덜 핫하면 30분 등).

> **키별 TTL**을 다르게 주고 싶다면 보통 “캐시 이름”을 분리해서 운용합니다.
> (스프링 캐시 표준만으로 키 단위 TTL을 지정하긴 어려움)

---

# 5) condition / unless 의 차이 (언제 평가되나?)

* `condition`: **메서드 실행 전** 평가. `false`면 캐싱 자체를 건너뜀.

  ```kotlin
  @Cacheable(condition="#topN <= 100")
  ```
* `unless`: **메서드 실행 후(결과가 나온 뒤)** 평가. `true`면 **저장하지 않음**.

  ```kotlin
  @Cacheable(unless="#result.isEmpty()")
  ```

---

# 6) 동시성: cache stampede(동시 미스 폭주) 방지

* 동일 키로 동시에 많은 스레드가 들어오면 **한꺼번에 미스**가 나고 DB가 몰릴 수 있어요.
* 해결: `sync = true`

  ```kotlin
  @Cacheable(cacheNames=["popularProducts"], key="...", sync=true)
  ```

    * 같은 키에 대해 **한 스레드만** 실제 로딩을 수행, 나머지는 그 결과를 공유

---

# 7) 트랜잭션과의 관계(중요 포인트)

* 기본적으로 캐시 put/evict는 **메서드 실행 흐름 안에서 즉시** 수행돼요.
  즉, 같은 트랜잭션 내에서 롤백되면 **캐시에 이미 들어갔을 수도 있음**.
* **트랜잭션 일관성**이 아주 중요하면:

    * `TransactionAwareCacheManagerProxy`(또는 `transactionAware=true`)로 **커밋 후에 반영**되게 하거나,
    * 캐시 연산을 커밋 이후 이벤트로 분리하는 전략을 고려.

---

# 8) Redis와 직렬화(Serialization)

* Redis는 값이 **바이트 배열**로 저장됩니다 → DTO 직렬화가 필요
* Boot 기본은 `GenericJackson2JsonRedisSerializer`(JSON) 사용이 일반적

    * `LocalDate`, `Instant` 등은 `JavaTimeModule` 등록 필요
* **키 직렬화**는 보통 `StringRedisSerializer`

    * 사람이 읽기 쉬운 평문 키(운영 문제 파악에 유리)

---

# 9) 언제 @Cacheable 을 쓰면 좋은가?

* **읽기 비중이 높고**(Read-heavy), **자주 반복**되는 쿼리
* **계산 비용이 큰 연산**의 결과
* **조금의 지연 최신성**이 허용되는 데이터(예: 인기상품 랭킹, 대시보드 집계)

> 반대로, **항상 최신이어야 하는 강한 일관성**이 필수라면 TTL을 매우 짧게 하거나, 캐시 대신 다른 전략을 고려.

---

# 10) 자주 하는 실수 & 베스트 프랙티스

* **Self-invocation**: 같은 클래스 내부 호출로 캐싱이 안 됨 → 분리 or self 주입
* **키 설계 부실**: 파라미터가 늘어날수록 키 규칙을 명확히(SpEL로 명시)
* **무효화 누락**: 데이터 변경 이벤트에서 `@CacheEvict`(또는 allEntries=true)로 정리
* **과도한 범위 캐시**: huge 파라미터 조합을 캐시 → **키 폭발** → 메모리 압박

    * `condition`으로 필터링, TTL 짧게, 캐시 이름 분리
* **가변 객체 캐시**: 캐시에서 꺼낸 뒤 수정하면 안 됨 → **불변 DTO** 추천(코틀린 `data class val` OK)

---

## 요약

* `@Cacheable`은 **프록시가 메서드를 가로채 캐시를 먼저 확인**하고, **히트면 본문을 건너뛰는** 방식으로 성능을 끌어올립니다.
* **키 설계**(SpEL), **TTL 설정**(CacheManager), **조건/예외(unless)**, **동시성(sync=true)**, **무효화(@CacheEvict)** 가 핵심 포인트.
* **Self-invocation**만 피하면 대부분의 케이스에서 “한 줄(@Cacheable)”로 큰 효과를 볼 수 있어요.
