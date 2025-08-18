
# RedissonClient 정리

## 📌 정의
**RedissonClient**는 Redisson 라이브러리의 메인 인터페이스로, Redis 서버와 연결하여
모든 Redisson 기능을 사용할 수 있는 진입점입니다.

---

## 📌 주요 기능

### 1. 분산락
Redis를 이용한 분산락을 쉽게 구현할 수 있습니다.
```java
RLock lock = redissonClient.getLock("lock:order:123");
lock.lock();
try {
    // 임계 구역
} finally {
    lock.unlock();
}
```

### 2. 분산 컬렉션
Redis 기반의 Java 컬렉션을 제공합니다.
```java
RMap<String, String> map = redissonClient.getMap("myMap");
map.put("key", "value");
```
지원 컬렉션: `RList`, `RSet`, `RQueue`, `RDeque`, `RSortedSet` 등

### 3. Pub/Sub
Redis Pub/Sub 기능을 활용할 수 있습니다.
```java
RTopic topic = redissonClient.getTopic("chat");
topic.publish("Hello World!");
```

### 4. 원격 서비스 호출
Redis를 매개로 한 RPC(Remote Procedure Call) 기능을 제공합니다.

### 5. 기타 동기화 도구
- CountDownLatch
- Semaphore
- RateLimiter
- 기타 분산 환경 동기화 기능

---

## 📌 관계 그림
```
[Spring Bean] RedissonClient
       ↓
[Redis Server] (단일 / Cluster / Sentinel)
```
- **RedissonClient**는 Redis 서버와의 연결을 유지하고
- 모든 락, 자료구조, 메시징 기능을 이 객체를 통해 사용합니다.

---

## 📌 한 줄 요약
> **RedissonClient** = Redis와 연결된 Redisson의 메인 객체로,
> 락, 컬렉션, Pub/Sub, 동기화 도구 등 모든 기능을 사용할 수 있는 진입점
