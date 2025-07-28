# Kotlin Companion Object

이 문서는 Kotlin에서의 `companion object`의 개념, 사용 이유, 예제 및 주의사항을 설명합니다.

---

## 🧾 1. 정의

`companion object`는 **클래스 내부에 정의된 객체**로, 해당 클래스의 **정적(static) 멤버처럼 동작**합니다. 자바의 `static` 키워드와 유사한 역할을 하며, 클래스 이름으로 접근할 수 있습니다.

```kotlin
class MyClass {
    companion object {
        const val VERSION = "1.0"

        fun create(): MyClass = MyClass()
    }
}

fun main() {
    println(MyClass.VERSION)           // 1.0
    val instance = MyClass.create()   // MyClass의 인스턴스 생성
}
```

---

## 🛠 2. 사용하는 이유

| 목적           | 설명                                                  |
| ------------ | --------------------------------------------------- |
| 정적 메서드처럼 사용  | 클래스 이름으로 접근 가능 (`MyClass.create()`)                 |
| 팩토리 메서드 제공   | 생성자 대신 여러 버전의 생성 메서드를 제공 (`of()`, `from()`)         |
| 싱글톤 객체와의 차별화 | `object`는 완전한 싱글톤, `companion object`는 클래스의 정적 컨텍스트 |
| 인터페이스 구현 가능  | `companion object`는 인터페이스 구현도 가능                    |

---

## 🔄 3. 팩토리 메서드와의 관계

Static Factory Method 패턴에서 자주 사용되며, **생성자 대신 가독성 좋은 이름으로 인스턴스를 생성**합니다.

```kotlin
class User private constructor(val name: String) {
    companion object {
        fun of(name: String): User = User(name)
        fun anonymous(): User = User("anonymous")
    }
}

val user1 = User.of("은지")
val user2 = User.anonymous()
```

---

## ⚠️ 주의사항

- `companion object`도 **실제로는 객체**이기 때문에 초기화 코드나 상태를 가질 수 있습니다.
- 자바와 다르게 클래스 정적 멤버가 아닌 `싱글톤 객체`라는 점을 이해하고 사용해야 합니다.
- `const val`은 **기본형 상수**에서만 사용 가능합니다.

---

## ✅ 요약

| 항목       | 설명                          |
| -------- | --------------------------- |
| 위치       | 클래스 내부                      |
| 접근 방식    | 클래스명.멤버 (`MyClass.VERSION`) |
| 활용 예     | 팩토리 메서드, 정적 상수, 인터페이스 구현 등  |
| 자바 대응 개념 | `static`                    |

`companion object`는 Kotlin의 객체 지향과 함수형 프로그래밍 사이를 자연스럽게 연결해주는 핵심 기능 중 하나입니다.

