Spring에서 \*\*`ApplicationEventPublisher`\*\*와 \*\*`@TransactionalEventListener`\*\*는 **이벤트 기반 아키텍처**를 구현할 때 핵심적으로 사용되는 도구입니다. 각 개념을 자세히 설명해볼게요.

---

## **1. `ApplicationEventPublisher` – 이벤트 발행기**

### **개념**

* Spring의 **이벤트 발행 API**로, 애플리케이션 내에서 **이벤트 객체를 발행(Publish)** 할 수 있게 해주는 인터페이스입니다.
* 발행된 이벤트는 해당 이벤트 타입을 구독(Listen)하는 리스너에게 전달됩니다.

---

### **특징**

| 항목    | 설명                                        |
| ----- | ----------------------------------------- |
| 역할    | 이벤트를 발행하여 리스너로 전달                         |
| 위치    | 트랜잭션 내부 또는 외부 모두에서 사용 가능                  |
| 호출 방식 | 동기(synchronous) 호출이 기본, 비동기는 `@Async`로 가능 |

---

### **사용 예시 (Kotlin)**

```kotlin
@Service
class ReservationService(
    private val eventPublisher: ApplicationEventPublisher
) {
    fun reserve(bookId: Long, userId: Long) {
        // 1. 예약 핵심 로직 실행 (DB 저장 등)
        // 2. 이벤트 발행
        eventPublisher.publishEvent(BookReservedEvent(bookId, userId))
    }
}
```

---

## **2. `@TransactionalEventListener` – 트랜잭션 연동 리스너**

### **개념**

* Spring에서 **트랜잭션 상태에 따라 이벤트를 처리할 타이밍을 지정할 수 있게 하는 애너테이션**입니다.
* 주로 **AFTER\_COMMIT**을 사용하여, 트랜잭션이 정상적으로 커밋된 뒤에 이벤트를 안전하게 처리하도록 합니다.

---

### **phase 옵션**

| 옵션                 | 실행 시점               | 사용 예시                |
| ------------------ | ------------------- | -------------------- |
| `BEFORE_COMMIT`    | 트랜잭션 커밋 직전          | 커밋 전에 꼭 실행해야 하는 로직   |
| `AFTER_COMMIT`     | 트랜잭션 커밋 완료 후        | 알림 발송, 로그 전송 등 부가 로직 |
| `AFTER_ROLLBACK`   | 트랜잭션 롤백 후           | 롤백 알림이나 에러 로깅        |
| `AFTER_COMPLETION` | 트랜잭션 완료 후(성공/실패 모두) | 후처리 작업               |

---

### **사용 예시 (Kotlin)**

```kotlin
@Component
class BookReservedListener(
    private val notificationService: NotificationService
) {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handle(event: BookReservedEvent) {
        // 트랜잭션 커밋이 끝난 뒤 실행됨
        notificationService.send(event.userId, "예약이 완료되었습니다.")
    }
}
```

---

## **왜 함께 쓰는가**

| 요소                                | 역할               | 효과                        |
| --------------------------------- | ---------------- | ------------------------- |
| **`ApplicationEventPublisher`**   | 이벤트 발행           | 핵심 로직과 부가 로직의 **관심사 분리**  |
| **`@TransactionalEventListener`** | 트랜잭션 완료 후 이벤트 처리 | **데이터 정합성 보장** 및 장애 전파 방지 |

---

## **실제 시나리오**

예를 들어 **도서 예약 시스템**에서:

1. **트랜잭션 내부**

    * 예약 정보를 DB에 저장
    * 재고 수량 감소
    * 커밋
2. **AFTER\_COMMIT 이벤트**

    * 예약 완료 알림 발송
    * 예약 통계 적재
    * 분석 시스템에 이벤트 전송

이렇게 하면:

* 핵심 로직은 가볍고 빠르게 끝남
* 부가 로직 실패가 트랜잭션에 영향을 주지 않음
