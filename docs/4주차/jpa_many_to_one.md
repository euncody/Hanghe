# 📘 JPA 연관관계 매핑: `@ManyToOne` 개념 정보

## 🔗 개요

`@ManyToOne`은 JPA에서 **N:1 관계**를 표현할 때 사용하는 어노프이션입니다.\
예를 들어, 여러 개의 주문(Order)은 하나의 사용자(User)에 속할 수 있습니다.

```kotlin
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id")
val user: UserEntity
```

---

## 💡 사용 이유

| 이유             | 설명                                          |
| -------------- | ------------------------------------------- |
| 🔍 도메인 탐색 용이   | `order.user.name` 처럼 객체 간 관계를 따라 데이터에 접근 가능 |
| 🛠 JPA가 관계를 관리 | 연관된 객체 저장/삭제 시 cascade 처리 가능                |
| 🧠 객체 지향 모델링   | 데이터베이스 관계를 코드로 자연스럽게 표현                     |

---

## 🔄 관계 예시

### ✅ 관계 설명

- 주문 (`OrderEntity`) — 다대일 — 사용자 (`UserEntity`)
- DB에는 `user_id` 컬럼으로 연결됨 (외래 키)

### ✅ JPA 매핑 코드 예시

```kotlin
@Entity
class OrderEntity(
    ...
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = ForeignKey(name = "order_ibfk_1"))
    val user: UserEntity
)
```

---

## ⚠️ 실무에서의 주의사항

| 항목         | 설명                                                   |
| ---------- | ---------------------------------------------------- |
| 🚧 N+1 문제  | 지역 로딩 시 반복 조회 발생 → `@BatchSize`, `JOIN FETCH` 등으로 해결 |
| 🔁 순환 참조   | 양방향 매핑 시 `toString()`, JSON 지리화에서 무한 루프 주의           |
| 🔒 연관관계 생보 | 다른 경우 `userId: String`만 쓰고 연관관계 생보와 구분되기도            |

---

## 📌 결론

- `@ManyToOne`은 객체 간 관계를 명확히 표현하고, 비지단시 로직을 더 지구화할 수 있게 도움을 줍니다.
- 하지만 성능 이슈 또는 복지도 증가를 고분해 **uc815말 필요한 경우에만 사용**하는 것이 좋습니다.

---

## 🗂 참고 구조

```plaintext
OrderEntity
 └── user: UserEntity (ManyToOne)
UserEntity
 └── userId: String (PK)
```

