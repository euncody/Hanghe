# Docker + MySQL + Redis + Spring Boot 연동 설정

이 문서는 Docker 환경에서 **MySQL**과 **Redis** 컨테이너를 설정하고 Spring Boot 애플리케이션과 연동하는 방법을 정리한 가이드입니다.

---

## 1. docker-compose.yml 설정

다음은 MySQL과 Redis를 동시에 띄우는 docker-compose 설정 예시입니다.  
이 예시는 MySQL을 로컬 3307 포트로, Redis를 로컬 6379 포트로 연결하도록 구성합니다.

```yaml
version: "3.9"

services:
  mysql:
    image: mysql:8.0
    container_name: hh_mysql
    command:
      - --default-authentication-plugin=mysql_native_password
      - --character-set-server=utf8mb4
      - --collation-server=utf8mb4_unicode_ci
    environment:
      TZ: Asia/Seoul
      MYSQL_ROOT_PASSWORD: root1234
      MYSQL_DATABASE: myapp
      MYSQL_USER: app
      MYSQL_PASSWORD: app1234
    ports:
      - "3307:3306"             # 로컬 3307 → 컨테이너 3306
    volumes:
      - db_data:/var/lib/mysql
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "127.0.0.1", "-uroot", "-proot1234"]
      interval: 5s
      timeout: 3s
      retries: 20

  redis:
    image: redis:7-alpine
    container_name: hh_redis
    command: ["redis-server","--appendonly","yes","--requirepass","redis1234"]
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
    healthcheck:
      test: ["CMD", "redis-cli", "-a", "redis1234", "PING"]
      interval: 5s
      timeout: 3s
      retries: 20

volumes:
  db_data:
  redis_data:
```

---

## 2. 컨테이너 실행 및 확인 명령어

```bash
# 컨테이너 실행
docker compose up -d

# 현재 실행 중인 컨테이너 확인
docker ps

# MySQL 접속/확인
docker exec -it hh_mysql mysql -uroot -proot1234 -e "SHOW DATABASES;"
docker exec -it hh_mysql mysql -uapp -papp1234 myapp -e "SHOW TABLES;"

# Redis 접속/확인
docker exec -it hh_redis redis-cli -a redis1234 PING
docker exec -it hh_redis redis-cli -a redis1234 SET foo bar
docker exec -it hh_redis redis-cli -a redis1234 GET foo
```

> 💡 `PONG`이 뜨면 Redis 서버가 정상 동작 중입니다.

---

## 3. Spring Boot application.yml 설정

Spring Boot가 Docker 컨테이너 내부의 MySQL과 Redis에 접속할 수 있도록 `application.yml`을 수정합니다.

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3307/myapp?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8&serverTimezone=Asia/Seoul
    username: app
    password: app1234

  data:
    redis:
      host: 127.0.0.1
      port: 6379
      password: redis1234

  jpa:
    hibernate:
      ddl-auto: update   # 초기 개발 단계에서는 create 또는 create-drop 사용 가능
    properties:
      hibernate:
        format_sql: true
    show-sql: true
```

---

## 4. build.gradle.kts 의존성 추가

MySQL과 Redis 연동을 위해 Spring Boot Starter 의존성을 추가합니다.

```kotlin
dependencies {
    // JPA + MySQL
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.mysql:mysql-connector-j")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
}
```

> Lettuce(기본) 또는 Jedis 클라이언트를 사용할 수 있습니다. Lettuce는 비동기 지원이 장점입니다.

---

## 5. 동작 확인 절차

1. **Docker 컨테이너 실행**  
   `docker compose up -d`로 MySQL과 Redis 컨테이너를 실행합니다.

2. **컨테이너 상태 확인**  
   `docker ps`로 hh_mysql, hh_redis 컨테이너가 실행 중인지 확인합니다.

3. **MySQL 연결 확인**  
   `docker exec`로 MySQL에 접속 후 `SHOW TABLES;` 명령어를 실행해 봅니다.

4. **Redis 연결 확인**  
   `docker exec`로 Redis에 접속 후 `PING`, `SET`, `GET` 명령어로 동작을 확인합니다.

5. **Spring Boot 실행**  
   애플리케이션을 실행해 MySQL/Redis 접속 로그와 동작을 확인합니다.

---

## 6. 참고 사항
- 로컬에서만 개발하는 경우 `127.0.0.1`로 접속해도 되지만,  
  Docker 내부 컨테이너끼리 통신할 때는 `mysql`, `redis` 같은 서비스명을 사용합니다.
- 운영 환경에서는 root 계정 사용을 피하고, 환경변수로 비밀번호를 관리하는 것이 안전합니다.
- Redis 비밀번호는 `--requirepass` 옵션으로 설정하며, 보안상 외부 노출을 피해야 합니다.
