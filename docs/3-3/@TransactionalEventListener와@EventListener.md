Spring에서 이벤트를 구독할 때 주로 쓰는 애너테이션은 \*\*`@EventListener`\*\*와 **`@TransactionalEventListener`** 두 가지입니다.
둘 다 “이벤트 리스너”라는 공통점이 있지만, **트랜잭션과의 연동 여부**에서 큰 차이가 있습니다.

---

## **1. `@EventListener`**

### **개념**

* Spring이 제공하는 **기본 이벤트 리스너 애너테이션**
* **트랜잭션 상태와 상관없이** 이벤트가 발행되면 바로 실행됩니다.

---

### **특징**

| 항목          | 설명                           |
| ----------- | ---------------------------- |
| **트랜잭션 연동** | 없음 – 트랜잭션의 성공/실패 여부와 관계없이 실행 |
| **실행 시점**   | 이벤트 발행 직후(동기)                |
| **비동기 지원**  | `@Async`를 함께 사용하면 가능         |

---

### **예시 (Kotlin)**

```kotlin
@Component
class SimpleListener {
    @EventListener
    fun handle(event: BookReservedEvent) {
        println("예약 이벤트 발생: ${event.bookId}")
    }
}
```

---

## **2. `@TransactionalEventListener`**

### **개념**

* Spring이 제공하는 **트랜잭션 연동 리스너**
* **트랜잭션 상태(커밋, 롤백 등)에 따라 이벤트 실행 시점을 제어**할 수 있습니다.

---

### **특징**

| 항목          | 설명                                          |
| ----------- | ------------------------------------------- |
| **트랜잭션 연동** | 가능 – AFTER\_COMMIT, AFTER\_ROLLBACK 등 단계 지정 |
| **실행 시점**   | 기본적으로 AFTER\_COMMIT (커밋 완료 후 실행)            |
| **데이터 정합성** | 트랜잭션이 실패하면 이벤트 실행도 자동으로 취소                  |

---

### **예시 (Kotlin)**

```kotlin
@Component
class TransactionalListener(
    private val notificationService: NotificationService
) {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handle(event: BookReservedEvent) {
        // DB 트랜잭션이 성공적으로 커밋된 후에만 실행
        notificationService.send(event.userId, "예약이 완료되었습니다.")
    }
}
```

---

## **3. 두 애너테이션의 비교**

| 항목          | `@EventListener`          | `@TransactionalEventListener` |
| ----------- | ------------------------- | ----------------------------- |
| **트랜잭션 연동** | 없음                        | 있음                            |
| **실행 시점**   | 이벤트 발행 직후 실행              | 트랜잭션 단계(커밋, 롤백 등)에 맞춰 실행      |
| **데이터 정합성** | 보장 어려움 (롤백돼도 이미 실행될 수 있음) | AFTER\_COMMIT으로 정합성 보장 가능     |
| **사용 예시**   | 단순 로깅, 모니터링, 트랜잭션 외부 이벤트  | 알림 발송, 통계 적재, 외부 API 호출 등     |

---

## **4. 선택 기준**

| 상황                                    | 추천 애너테이션                                        | 이유         |
| ------------------------------------- | ----------------------------------------------- | ---------- |
| 트랜잭션 성공 여부와 상관없는 단순 이벤트 처리            | `@EventListener`                                | 구현 간단      |
| 트랜잭션 커밋 후에만 실행해야 하는 로직 (예: 알림, 로그 적재) | `@TransactionalEventListener`                   | 데이터 정합성 보장 |
| 트랜잭션 롤백 시 추가 처리 필요                    | `@TransactionalEventListener` (AFTER\_ROLLBACK) | 롤백 후 처리 가능 |

---

## **정리**

* `@EventListener`

    * 단순 이벤트 구독용, 트랜잭션과 무관
    * 주로 로깅이나 통계 수집처럼 “실패해도 괜찮은” 작업에 적합
* `@TransactionalEventListener`

    * 트랜잭션 상태와 연동
    * **AFTER\_COMMIT** 패턴을 사용하면, 핵심 로직이 성공적으로 커밋된 후 부가 로직을 안전하게 실행 가능

---

예를 들어, **예약 완료 알림 발송**은 `@TransactionalEventListener`가 더 적합합니다.
왜냐하면 예약 데이터가 DB에 성공적으로 저장되기 전에 알림을 발송하면, 나중에 예약이 롤백되었을 때 사용자에게 잘못된 알림이 가기 때문이죠.
