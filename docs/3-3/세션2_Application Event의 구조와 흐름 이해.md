이 세션은 **이벤트 기반 아키텍처**를 통해 **핵심 로직을 깔끔하게 유지**하고 **트랜잭션 안정성을 높이는 방법**을 설명하는 내용이에요. 하나씩 풀어서 설명해볼게요.

---

## **1. 이벤트 발행과 구독의 개념**

### **이벤트 발행(Publish)**

어떤 일이 발생했음을 알리는 신호를 “발행”하는 것.\
예:

* 도서 예약이 성공했을 때 → “예약 완료 이벤트” 발행
* 결제가 승인되었을 때 → “결제 완료 이벤트” 발행

```java
// Spring 예시
applicationEventPublisher.publishEvent(new BookReservedEvent(bookId, userId));
```

---

### **이벤트 구독(Subscribe)**

이벤트가 발행되면, 그걸 “구독”하고 있던 로직이 실행되는 것.
예:

* 예약 완료 이벤트 → 알림 메시지 발송 로직 실행
* 결제 완료 이벤트 → 영수증 메일 발송 로직 실행

```java
// Spring 예시
@Component
public class BookReservedListener {
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(BookReservedEvent event) {
        notificationService.send(event.getUserId(), "예약이 완료되었습니다.");
    }
}
```

---

이 구조의 장점은 **“핵심 로직”과 “부가 로직”이 분리**된다는 거예요.
예약 기능 자체는 “예약 데이터 저장”에만 집중하고, 부가 작업은 이벤트를 통해 별도로 처리합니다.

---

## **2. AFTER\_COMMIT 시점에서의 이벤트 처리**

트랜잭션이 완료되기 전에 이벤트를 처리하면 이런 문제가 생길 수 있어요:

* 트랜잭션이 나중에 롤백되면 이미 발송한 알림이나 외부 호출을 되돌릴 수 없음
* 데이터 불일치 가능성이 높아짐

그래서 **AFTER\_COMMIT** 시점, 즉 트랜잭션이 성공적으로 커밋된 이후에 이벤트를 처리해야 합니다.

Spring에서는 이렇게 구현하죠:

```java
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
public void handle(BookReservedEvent event) {
    // 트랜잭션이 커밋된 후 실행
}
```

---

## **3. 관심사 분리와 느슨한 결합**

이벤트 기반 구조는 \*\*Publisher(발행자)\*\*와 \*\*Subscriber(구독자)\*\*가 서로를 알 필요가 없는 **느슨한 결합(Loose Coupling)** 구조를 제공합니다.

| 구조         | 발행자                  | 구독자                   |
| ---------- | -------------------- | --------------------- |
| **이전 구조**  | 예약 로직에서 직접 알림 서비스 호출 | 알림 로직이 예약 서비스에 강하게 결합 |
| **이벤트 구조** | 예약 로직은 “이벤트 발행”만 함   | 알림 로직은 “이벤트 수신”만 함    |

이렇게 하면:

* 코드 유지보수가 쉬워지고
* 새로운 기능 추가 시 기존 핵심 로직을 건드리지 않아도 됩니다.

---

## **4. 구현 포인트**

### **Spring**

* 이벤트 발행: `ApplicationEventPublisher`
* 이벤트 구독: `@TransactionalEventListener`
* 트랜잭션과 자연스럽게 연동 가능

---

## **5. 구조 예시**

**도서 예약 시나리오**

1. 예약 서비스: 예약 성공 시 `BookReservedEvent` 발행
2. 알림 서비스: 이벤트 구독 후 푸시 알림 발송
3. 통계 서비스: 이벤트 구독 후 예약 통계 집계

```text
[예약 서비스] -- 이벤트 발행 --> [알림 서비스]
                           --> [통계 서비스]
```

---

## **정리**

* **핵심 메시지**

    * 핵심 로직은 트랜잭션 안에서 **간결하게 유지**
    * 부가 로직은 이벤트로 분리하여 AFTER\_COMMIT 시점에 처리
* **장점**

    * 데이터 정합성 유지
    * 코드 간결화
    * 새로운 기능 추가 시 확장 용이
