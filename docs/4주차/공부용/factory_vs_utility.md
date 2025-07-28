# Factory Method vs Static Factory Method

이 문서는 객체 생성 시 사용되는 **Factory Method**와 **Static Factory Method**의 차이점과 사용 시 고려사항을 정리합니다.

---

## 🏭 1. Factory Method Pattern

### 정의
- Factory Method Pattern은 **객체 생성을 서브 클래스에서 처리하도록 위임**하는 디자인 패턴입니다.
- 생성 로직을 하위 클래스에 맡김으로써 상위 클래스는 생성되는 객체의 구체적인 클래스를 몰라도 됩니다.

### 특징
- **상속**을 통한 확장 가능성 제공
- 인터페이스나 추상 클래스를 통해 객체 생성 구조화
- 객체 생성을 **런타임 시 결정** 가능

### 예시 (Kotlin)
```kotlin
interface Product {
    fun use(): String
}

class Book : Product {
    override fun use() = "책 사용"
}

class BookFactory {
    fun create(): Product = Book()
}
```

---

## ⚙️ 2. Static Factory Method (정적 팩토리 메서드)

### 정의
- 객체 생성을 위한 **정적(static) 메서드**를 제공하여, 생성자 대신 사용할 수 있도록 하는 기법입니다.

### 특징
- 메서드 이름으로 **생성 목적 명확화** 가능
- **객체 재사용** 또는 **싱글톤** 등의 제어 가능
- 하위 클래스를 만들기보단, **내부 구현을 캡슐화**

### 예시 (Kotlin)
```kotlin
class User private constructor(val name: String) {
    companion object {
        fun of(name: String): User = User(name)
        fun anonymous(): User = User("anonymous")
    }
}
```

---

## 🔍 비교 요약

| 항목 | Factory Method Pattern | Static Factory Method |
|------|-------------------------|------------------------|
| 키워드 | 클래스 상속 / 오버라이딩 | static-like 함수 (companion) |
| 확장성 | 좋음 (다형성 활용) | 한정적 |
| 사용 예 | 라이브러리 확장, 인터페이스 구현 | 간단한 객체 생성, 싱글톤 |
| 예시 | `create(): Product` | `User.of("철수")` |

---

## 📌 정리
- **Factory Method Pattern**: 상속 기반의 유연한 확장 가능 구조
- **Static Factory Method**: 간단하고 명확한 객체 생성 방식

두 방식은 배타적이지 않으며, 상황에 따라 함께 사용할 수도 있습니다.

