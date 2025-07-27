
# 🛠️ Gradle Kotlin DSL 기본 템플릿 (`build.gradle.kts`)

이 문서는 Kotlin 기반 프로젝트를 위한 Gradle Kotlin DSL 템플릿입니다.  
초보자도 쉽게 사용할 수 있도록 **기본적인 설정과 주석**을 포함했습니다.

---

## 📁 기본 구조

```
project-root/
 ├── build.gradle.kts   ← 이 파일!
 ├── settings.gradle.kts
 └── src/
      └── main/
          └── kotlin/
```

---

## 📝 build.gradle.kts

```kotlin
plugins {
    // Kotlin JVM 플러그인
    kotlin("jvm") version "1.9.10"
    // Java 애플리케이션 실행을 위한 플러그인
    application
}

group = "com.example"   // 패키지 네임
version = "1.0.0"       // 버전 정보

// 의존성 받을 저장소 설정
repositories {
    mavenCentral()
}

// 사용할 라이브러리 명시
dependencies {
    // Kotlin 표준 라이브러리
    implementation(kotlin("stdlib"))

    // JUnit 테스트 라이브러리
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

// 실행할 메인 클래스 지정
application {
    mainClass.set("com.example.MainKt") // Main 함수가 있는 위치
}

// 테스트 설정 (JUnit 5 사용 시)
tasks.test {
    useJUnitPlatform()
}
```

---

## 📘 settings.gradle.kts

```kotlin
rootProject.name = "my-kotlin-app"
```

---

## ✅ 이 템플릿으로 할 수 있는 것

- Kotlin 코드 작성 및 실행
- 표준 라이브러리 사용
- 테스트 코드 작성
- `./gradlew run`으로 실행 가능

---

## 🚀 실행 방법

```bash
./gradlew build        # 빌드
./gradlew run          # 실행
./gradlew test         # 테스트
```

---

## 🧠 참고

- `mainClass.set(...)` 위치에 실제 프로젝트의 메인 클래스 경로를 지정하세요.
- 더 많은 플러그인은 [plugins.gradle.org](https://plugins.gradle.org/)에서 확인할 수 있어요.

---
