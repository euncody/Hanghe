좋은 질문입니다! 정확히 말하면 **"Redis를 이용한 분산 락 구현"**이에요.

## 분산 락이란? 🌐

**분산 락(Distributed Lock)**은 여러 개의 서버나 프로세스가 **같은 자원에 동시 접근하는 것을 제어**하는 메커니즘입니다.

### 일반 락 vs 분산 락

```
🏠 일반 락 (JVM 내부)
┌─────────────────┐
│   서버 A        │
│  Thread 1 ──┐   │
│  Thread 2 ──┤락 │  ← 같은 JVM 안에서만 동작
│  Thread 3 ──┘   │
└─────────────────┘

🌍 분산 락 (여러 서버 간)
┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│   서버 A     │    │   서버 B     │    │   서버 C     │
│ Thread 1     │    │ Thread 1     │    │ Thread 1     │
│ Thread 2     │    │ Thread 2     │    │ Thread 2     │
└──────┬───────┘    └──────┬───────┘    └──────┬───────┘
       │                   │                   │
       └───────────────────┼───────────────────┘
                          │
                   ┌─────────────┐
                   │ Redis 서버  │  ← 중앙에서 락 관리
                   │    락 저장  │
                   └─────────────┘
```

## Redis가 분산 락 역할을 하는 이유 🔑

### 1. **중앙 집중식 저장소**
```kotlin
// 모든 서버가 같은 Redis를 바라봄
val lock = redissonClient.getLock("lock:user:123")
```

- 서버 A, B, C 모두 **같은 Redis**에서 락 상태를 확인
- 한 서버에서 락을 획득하면, 다른 서버들은 대기

### 2. **원자적 연산 지원**
```redis
# Redis의 SET 명령어는 원자적
SET lock:user:123 "server-A" NX EX 10
```
- `NX`: 키가 없을 때만 설정 (Not eXists)
- `EX`: 만료 시간 설정
- 여러 서버가 동시에 요청해도 **하나만 성공**

### 3. **자동 만료 기능**
```kotlin
// 10초 후 자동으로 락 해제
lock.tryLock(2000, 10000, TimeUnit.MILLISECONDS)
```

## 실제 동작 예시 📝

### 상황: 포인트 충전 (서버 3대 운영 중)

```kotlin
// 모든 서버에서 같은 코드 실행
@RedisLock(key = "user:point:#{#userId}")
fun chargePoint(userId: Long, amount: Int) {
    val user = userRepository.findById(userId)
    user.point += amount  // 동시성 문제 발생 가능 지점
    userRepository.save(user)
}
```

### 시간순 동작:
```
시간 0초:
서버A: chargePoint(userId=123, amount=100) 호출
서버B: chargePoint(userId=123, amount=200) 호출  ← 같은 사용자!

시간 0.001초:
서버A: Redis에 "lock:user:point:123" 키로 락 요청 → ✅ 성공
서버B: Redis에 "lock:user:point:123" 키로 락 요청 → ❌ 실패 (대기)

시간 0.002초~3초:
서버A: 포인트 충전 로직 실행 (DB 읽기 → 계산 → DB 저장)
서버B: 락 획득까지 대기...

시간 3초:
서버A: 작업 완료, 락 해제
서버B: 락 획득 성공, 포인트 충전 로직 실행 시작
```

## Redis 분산 락의 장점 👍

### 1. **간단한 구현**
```kotlin
// Redisson 사용시 매우 간단
val lock = redisson.getLock("myLock")
if (lock.tryLock()) {
    // 작업 수행
}
```

### 2. **높은 성능**
- Redis는 메모리 기반이라 빠름
- 단순한 SET/DELETE 연산

### 3. **자동 해제**
- 서버가 죽어도 TTL로 자동 해제
- 데드락 방지

## 한계점도 있어요 ⚠️

### 1. **Redis 장애 시 문제**
```
Redis 서버 다운 → 모든 락 기능 중단 😱
```

### 2. **네트워크 지연**
```
락 획득/해제마다 네트워크 통신 필요
```

### 3. **정확성 보장의 어려움**
```
# Redis Cluster나 Master-Slave 환경에서
# 복제 지연으로 인한 문제 가능성
```

## 그래서 결론은? 🎯

**Redis 락 = 분산 락의 한 종류**

- ✅ 여러 서버 간 동시성 제어 가능
- ✅ 구현이 상대적으로 간단
- ✅ 성능이 좋음
- ⚠️ Redis 의존성과 한계점 존재

실무에서는 **"완벽하지는 않지만 실용적인"** 분산 락 솔루션으로 널리 사용되고 있어요!

더 엄격한 분산 락이 필요하다면 **Redlock 알고리즘**이나 **ZooKeeper**, **etcd** 같은 다른 솔루션도 고려할 수 있습니다.