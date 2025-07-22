
# 📘 Kotlin DSL 

## 🧠 DSL이란?

> **DSL (Domain-Specific Language)**  
→ 어떤 목적에 맞게 설계된 "작은 전용 언어"예요.

예시:
- SQL → 데이터 질의용 DSL
- HTML → 화면 구조 표현 DSL
- Gradle → 빌드 설정 DSL

---

## 🚀 Kotlin DSL이란?

**Kotlin DSL**은 Kotlin 문법으로 DSL을 만든 것 또는  
Gradle처럼 이미 만들어진 DSL을 **Kotlin 언어로 사용하는 것**을 말해요.

---

## 🍜 비유: 라면 레시피 DSL

기존 코드:
```kotlin
val name = "김은지"
val age = 22
println("Hello $name, you're $age")
```

DSL처럼 바꾸면:
```kotlin
greet {
    name = "김은지"
    age = 22
}
```

→ 코드가 **문장처럼 읽히는 것**이 바로 DSL의 매력이에요!

---

## 💡 Kotlin DSL은 어디에 쓰일까?

| 활용 예 | 설명 |
|--------|------|
| ✅ Gradle 빌드 설정 | `build.gradle.kts` |
| ✅ Jetpack Compose | `Text("Hello")` 같은 UI 선언 |
| ✅ HTML DSL | `body { h1 { +"Title" } }` |
| ✅ 직접 만든 DSL | 위 예제처럼 나만의 DSL 만들기 가능 |

---

## 🔍 Gradle Kotlin DSL vs Groovy DSL

| 항목 | Kotlin DSL (`.kts`) | Groovy DSL (`.gradle`) |
|------|----------------------|-------------------------|
| 언어 기반 | Kotlin | Groovy |
| 정적 타입 | ✅ (자동완성 O) | ❌ (자동완성 약함) |
| IDE 지원 | 아주 좋음 | 상대적으로 떨어짐 |
| 커스터마이징 | 자유롭고 안전함 | 유연하지만 에러 탐지 어려움 |

---

## ✨ 예제: Gradle에서 사용하는 Kotlin DSL

```kotlin
plugins {
    kotlin("jvm") version "1.9.10"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

application {
    mainClass.set("com.example.MainKt")
}
```

→ 이게 바로 `build.gradle.kts` 파일이에요!

---

## ✅ 정리

| 질문 | 답변 |
|------|------|
| DSL이 뭐야? | 특정 목적에 맞춘 작은 언어 |
| Kotlin DSL은? | Kotlin으로 만든 DSL 또는 사용하는 것 |
| 왜 써? | 코드가 자연스럽고 IDE 지원 좋음 |
| 어디에 써? | Gradle, Compose, HTML 등 |

---

## 🔥 보너스: 나만의 DSL 예시

```kotlin
fun pizza(block: Pizza.() -> Unit) {
    val p = Pizza().apply(block)
    println("도우: ${p.dough}, 토핑: ${p.toppings}")
}

class Pizza {
    var dough: String = ""
    val toppings = mutableListOf<String>()
    fun topping(name: String) {
        toppings.add(name)
    }
}

pizza {
    dough = "thin crust"
    topping("cheese")
    topping("pepperoni")
}
```

---

## 📚 참고

- 공식 문서: https://docs.gradle.org/current/userguide/kotlin_dsl.html
- Kotlin DSL 소개: https://kotlinlang.org/docs/type-safe-builders.html

---
