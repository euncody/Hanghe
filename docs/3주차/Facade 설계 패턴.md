# Facade 설계 패턴 (Facade Design Pattern)

## 개요

Facade 패턴은 복잡한 시스템 내부 구현을 숨기고, 외부에는 단순한 인터페이스만 제공하는 구조적 디자인 패턴이다. 여러 개의 클래스나 서브시스템을 묶어 하나의 진입점(Facade)을 만들어, 클라이언트가 더 쉽게 사용할 수 있도록 도와준다.

---

## 핵심 개념

* **복잡한 내부 로직**을 클라이언트가 몰라도 되도록 추상화
* \*\*하나의 고수준 클래스(Facade)\*\*가 여러 컴포넌트를 조정 (orchestration)
* **의존성 감소**, **유지보수성 향상**, **코드 가독성 증가**

---

## 구조 예시 (e-커머스 주문 처리)

### 기존 방식 (Facade 없이)

```kotlin
userService.checkUserStatus()
productService.checkStock()
paymentService.processPayment()
orderService.createOrder()
emailService.sendConfirmation()
```

### Facade 적용 후

```kotlin
class OrderFacade(
    private val userService: UserService,
    private val productService: ProductService,
    private val paymentService: PaymentService,
    private val orderService: OrderService,
    private val emailService: EmailService
) {
    fun placeOrder(request: OrderRequest) {
        userService.checkUserStatus(request.userId)
        productService.checkStock(request.productId)
        paymentService.processPayment(request.paymentInfo)
        orderService.createOrder(request)
        emailService.sendConfirmation(request.userId)
    }
}
```

---

## 장점

| 장점        | 설명                         |
| --------- | -------------------------- |
| 간단한 인터페이스 | 클라이언트는 복잡한 내부를 몰라도 됨       |
| 결합도 감소    | 외부와 내부 시스템 간의 의존성 줄어듦      |
| 유지보수 용이   | 내부 구조가 바뀌어도 외부 인터페이스 유지 가능 |

---

## 주의 사항

* **비즈니스 로직은 Facade에 넣지 말 것** → 각 Service에 유지
* **기능이 너무 많아지지 않도록 적절히 분리** (예: OrderFacade, PointFacade 등)

---

## 실전 적용 구조 (Spring 기준)

```plaintext
Controller → Facade → Service → Repository
```

* Controller: HTTP 요청 처리
* Facade: 여러 Service 호출 조정
* Service: 도메인별 비즈니스 로직 수행
* Repository: DB 연동

---

## 실제 Controller 적용 예시

```kotlin
@RestController
class OrderController(private val orderFacade: OrderFacade) {
    
    @PostMapping("/orders")
    fun placeOrder(@RequestBody request: OrderRequest) {
        orderFacade.placeOrder(request)
    }
}
```

---

## 결론

Facade 패턴은 복잡한 내부 로직을 클라이언트로부터 숨기고, 명확한 API를 제공하여 시스템의 유연성과 유지보수성을 높이는 데 큰 도움이 된다. 특히 도메인 간 협력이 많은 시스템(e.g. e-커머스)에서 매우 유용하게 활용된다.
