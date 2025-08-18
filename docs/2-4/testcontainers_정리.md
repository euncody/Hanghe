# Testcontainers 정리 및 사용 가이드

## ✅ Testcontainers란?
Testcontainers는 **Docker 컨테이너 기반의 통합 테스트 환경을 제공하는 라이브러리**로, 
테스트 실행 시 데이터베이스, 메시지 브로커 등을 격리된 상태로 실행할 수 있도록 지원한다.

- 격리된 테스트 환경 제공 → 테스트 간 데이터 간섭 없음
- 실제 서비스와 동일한 환경 재현 (ex. MySQL, Redis, Kafka 등)
- 테스트 후 컨테이너 자동 종료 → 클린한 테스트 보장

---

## 📦 주요 특징
| 기능                  | 설명                                                         |
|---------------------|------------------------------------------------------------|
| Docker 기반 테스트   | MySQL, PostgreSQL, Redis 등 다양한 서비스 컨테이너 제공      |
| 자동화된 환경 구성   | 테스트 실행 시 컨테이너 자동 실행 및 종료                   |
| Spring Boot 통합     | `@Testcontainers`, `@Container`로 간단하게 구성 가능         |
| 데이터 초기화 지원   | `withInitScript()`으로 초기 SQL 자동 실행 가능               |

---

## ⚙️ 사용 준비물

1. Docker 설치 및 실행
    - Windows/Mac: Docker Desktop 설치 및 실행
    - Linux: `sudo apt install docker.io`

2. Gradle 의존성 추가

<details>
<summary>build.gradle.kts</summary>

```kotlin
dependencies {
    testImplementation("org.testcontainers:junit-jupiter:1.19.1")
    testImplementation("org.testcontainers:mysql:1.19.1")
}
tasks.test {
    useJUnitPlatform()
}
```
</details>

3. `application-test.yml` 작성
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/test  # 무시됨, 컨테이너에서 override됨
    username: test
    password: test
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
```

---

## 🧪 사용 순서

1. 테스트 클래스에 설정 추가
```kotlin
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MyIntegrationTest {

    companion object {
        @Container
        val mysql = MySQLContainer<Nothing>("mysql:8.0.31").apply {
            withDatabaseName("testdb")
            withUsername("test")
            withPassword("test")
            withInitScript("init-test-db.sql")  // 초기화 SQL
        }

        @JvmStatic
        @DynamicPropertySource
        fun overrideProps(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", mysql::getJdbcUrl)
            registry.add("spring.datasource.username", mysql::getUsername)
            registry.add("spring.datasource.password", mysql::getPassword)
        }
    }

    // 테스트 코드 작성 ...
}
```

2. `init-test-db.sql` 파일 생성
- 위치: `src/test/resources/init-test-db.sql`
- 테이블 생성 및 가라 데이터 삽입

3. 테스트 실행
- Docker가 실행 중이어야 함
- 컨테이너 자동 실행 및 종료 확인

---

## 🚨 유의사항
- Docker가 실행 중이지 않으면 Testcontainers는 실패함
- Testcontainers는 테스트 속도보다 **격리된 신뢰성**에 초점
- 무거운 컨테이너는 테스트 속도에 영향 줄 수 있음

---

## 🔗 공식 문서
- [Testcontainers 공식 사이트](https://java.testcontainers.org)
- [지원 이미지 목록](https://www.testcontainers.org/modules)