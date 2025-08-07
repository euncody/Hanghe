
# 🔐 DB Lock 정리

## DB Lock이란?
> 여러 트랜잭션이 동시에 같은 데이터에 접근할 때, **충돌 방지**를 위해 데이터를 잠그는 것.

---

## 🧩 Lock의 종류

### 1. 🔄 트랜잭션 락 기준

| 종류               | 설명 |
|--------------------|------|
| **공유 락 (S Lock)** | 읽기 가능, 수정 불가 |
| **배타 락 (X Lock)** | 읽기+쓰기 가능, 다른 작업 불가 |

### 2. 🔂 락 범위 기준

| 락 범위         | 설명 |
|------------------|------|
| **행 락 (Row Lock)** | 특정 행만 잠금 |
| **테이블 락 (Table Lock)** | 테이블 전체 잠금 |

### 3. ⏳ 락 방식 기준

| 락 방식               | 설명 |
|-----------------------|------|
| **낙관적 락**         | 먼저 실행, 나중에 충돌 확인 |
| **비관적 락**         | 먼저 락 걸고 실행, 안전하지만 느림 |

---

## 🔧 SQL로 락 걸기

```sql
-- 공유 락 (읽기 전용)
SELECT * FROM products WHERE id = 1 LOCK IN SHARE MODE;

-- 배타 락 (쓰기 가능)
SELECT * FROM products WHERE id = 1 FOR UPDATE;
```

---

# 🖥️ Kotlin에서 락 걸기 (JPA)

## 1. 비관적 락 예시 (Pessimistic Lock)

```kotlin
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import javax.persistence.LockModeType

interface ProductRepository : CrudRepository<Product, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    fun findWithPessimisticLock(id: Long): Product?
}
```

### 설명:
- `PESSIMISTIC_WRITE` → 배타 락, 다른 트랜잭션 대기.
- 읽기 락은 `PESSIMISTIC_READ`.

---

## 2. 낙관적 락 예시 (Optimistic Lock)

```kotlin
import javax.persistence.*

@Entity
data class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var name: String,

    var stock: Int,

    @Version
    var version: Long = 0
)
```

### 사용법:

```kotlin
product.stock -= 1
productRepository.save(product)
```

- `@Version` → 저장 시 충돌 확인, 충돌 시 예외 발생.

---

## 🚨 예외 처리

| 상황           | 예외 |
|----------------|------|
| 낙관적 락 실패 | `OptimisticLockException` |
| 비관적 락 실패 | `PessimisticLockException` |

---

# 💡 요약

1. 락은 **동시 접근 충돌 방지**용 잠금 장치.
2. 공유 락(S): 읽기만 허용, 배타 락(X): 읽기+쓰기 가능.
3. 비관적 락: 먼저 락 → 안정적, 낙관적 락: 충돌 시 예외.
4. Kotlin에서는 `@Lock`, `@Version`으로 락 처리.

