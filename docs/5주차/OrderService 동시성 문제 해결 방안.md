# 동시성 문제 DB 해결 방안

## 1. 문제 식별
- **서비스 맥락**: 전자상거래 서비스의 `OrderService.createOrder()`에서 다수 동시 요청 시 재고 감소 로직에서 경쟁 상황 발생
- **증상**:
  - 다수 스레드가 같은 상품 재고(amount)를 조회 후 감소 → 전체 재고가 비일관 상태로 차감됨  
  - 일부 주문이 의도치 않게 예외(`IllegalStateException: 재고 부족`) 처리  
  - 테스트 결과, 성공 주문 수와 실제 남은 재고가 일치하지 않음

## 2. 분석
- **트랜잭션 경계**: `@Transactional` 내부에서 조회 → 감소 → 저장 순으로 실행되지만, 첫 조회 후 락이 없으므로 다른 트랜잭션 개입 가능
- **경쟁 지점**:  
  1. `select * from product where product_key = ?`  
  2. `product.amount -= qty`  
  3. `update product set amount = ? where product_key = ?`
- **문제 원인**:  
  - 트랜잭션 간 락 없음 → 갱신 시 충돌 발생  
  - 동시성 충돌 시 비일관 상태 또는 예외 누락

## 3. 해결
### 3.1 Optimistic Locking + 재시도 로직
- **구현**:
  1. `Product` 엔티티에 `@Version` 컬럼 추가  
  2. 저장 시 `OptimisticLockException` 발생하면 최대 3회 재시도  
```kotlin
fun decreaseStockWithRetry(id: Long, qty: Int, maxRetry: Int = 3) {
  repeat(maxRetry) { attempt ->
    try {
      val p = repo.findById(id).orElseThrow()
      p.decreaseStock(qty)
      repo.save(p)
      return
    } catch (ex: OptimisticLockException) {
      if (attempt == maxRetry - 1) throw ex
    }
  }
}
```
### 3.2 Pessimistic Locking
- **구현**:
```kotlin
@Transactional
fun createOrder(order: Order) {
  val p = em.find(Product::class.java, order.productKey,
                 LockModeType.PESSIMISTIC_WRITE)
  if (p.amount < order.quantity) throw IllegalStateException("재고 부족")
  p.amount -= order.quantity
  // ...
}
```
### 3.3 단일 쿼리 원자성 업데이트
- **쿼리 예시**:
```sql
UPDATE product 
SET amount = amount - :qty 
WHERE product_key = :key AND amount >= :qty;
```
- **특징**: 감소 및 검증을 동시에 처리, 간단하지만 비즈니스 로직 반영 제한

### 3.4 추가 권고 사항
- **트랜잭션 격리 수준**: `REPEATABLE_READ` 이상 설정 검토  
- **모니터링**: 충돌 빈도, 재시도 횟수, 응답 시간 실시간 모니터링  
- **추후 확장**: 메시지 큐 기반 비동기 처리, 파티셔닝, 샤딩
