
# 엔티티(Entity)와 도메인(Domain)에 대한 설명

## ✅ 요약

| 구분       | 정의 |
|------------|------|
| **엔티티(Entity)** | 고유한 ID(식별자)를 가지며, 변경 가능한 상태를 가진 객체 |
| **도메인(Domain)** | 시스템이 해결하려는 **업무 영역 자체** (예: 주문, 결제, 상품 등) |

---

## 🔍 1. 엔티티 (Entity)

- **DB 테이블과 1:1로 대응되는 경우가 많음**
- **식별자(ID)** 로 구별됨
- 동일한 속성 값을 가져도 **ID가 다르면 다른 객체**
- 상태가 바뀔 수 있음 (mutable)

### 예시

```kotlin
data class User(
    val id: Long,
    var name: String,
    var point: Int
)
```

> 이 `User`는 엔티티입니다.  
> 이유? → `id`로 식별 가능하고, `point`는 변할 수 있음.

---

## 🔍 2. 도메인 (Domain)

- **비즈니스의 핵심 개념과 규칙이 담긴 영역**
- 시스템이 처리하는 **문제의 대상**
- 엔티티, 밸류 객체, 도메인 서비스 등이 모두 여기에 속함

### 예시

전자상거래 시스템의 도메인 구성

```
도메인: 주문(Order)
├─ 엔티티: Order, OrderItem
├─ 밸류 객체: Money, Address
├─ 서비스: OrderService (비즈니스 로직 처리)
```

> "주문을 생성하면 재고를 차감하고, 포인트를 차감하며, 주문 내역을 저장한다"  
> 이 모든 흐름과 규칙이 **도메인 로직**에 해당합니다.

---

## ✅ 비교

| 항목 | Entity | Domain |
|------|--------|--------|
| 목적 | 고유한 객체 식별 및 상태 유지 | 비즈니스 개념 표현 및 로직 캡슐화 |
| 특징 | ID 존재, 상태 변화함 | 규칙 중심, 개념적 의미 강조 |
| 예시 | `User`, `Product`, `Order` | `OrderService`, `CouponPolicy` 등 포함 |
| 관계 | 도메인의 구성 요소 중 하나 | 더 넓은 개념 |

---

## ✅ 실전 예시

```kotlin
// Entity
data class Product(val id: Long, var name: String, var stock: Int)

// Domain 서비스
class OrderService {
    fun placeOrder(userId: Long, productId: Long, quantity: Int) {
        // 재고 확인, 결제 처리, 주문 저장 등의 도메인 로직
    }
}
```

---

## 🔚 요약 한 줄로

> **엔티티는 데이터를 가지는 객체이고, 도메인은 이 데이터를 둘러싼 비즈니스 의미와 로직을 담은 영역입니다.**
