
# Docker MySQL 연결 가이드 (로컬 MySQL 유지 + Docker는 다른 포트 사용)

이 문서는 **로컬 MySQL은 그대로 유지**하고, **Docker 컨테이너의 MySQL**을 **다른 포트(3307)** 로 실행하여
Spring Boot 애플리케이션과 연결하는 절차를 정리합니다.

---

## 1) 환경 개요
- Docker 이미지: `mysql:8.0`
- 컨테이너 이름: `hh_mysql`
- 포트 매핑: **호스트 3307 → 컨테이너 3306**
- 데이터베이스: `myapp`
- 계정: `app / app1234` (root 비밀번호: `root1234`)
- 볼륨: `db_data` (MySQL 데이터 영구 저장)

---

## 2) docker-compose.yml (프로젝트 루트에 생성)
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
      - "3307:3306"        # 호스트 3307 → 컨테이너 3306
    volumes:
      - db_data:/var/lib/mysql
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "127.0.0.1", "-uroot", "-proot1234"]
      interval: 5s
      timeout: 3s
      retries: 20

volumes:
  db_data:
```

실행:
```bash
cd <프로젝트_루트>
docker compose up -d
docker ps                # hh_mysql Up & 포트 3307 확인
docker logs -f hh_mysql  # (원하면) 로그 확인 후 Ctrl + C
```

---

## 3) 컨테이너 내부 MySQL 접속/확인
로컬에 `mysql` CLI가 없어도 `docker exec`로 바로 접속할 수 있습니다.

```bash
# DB 목록 확인 (root)
docker exec -it hh_mysql mysql -uroot -proot1234 -e "SHOW DATABASES;"

# app 계정으로 접속 (myapp DB)
docker exec -it hh_mysql mysql -uapp -papp1234 myapp

# (접속 후) 테이블 확인
SHOW TABLES;
```

---

## 4) Spring Boot 설정 (application.yml)
```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3307/myapp?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8&serverTimezone=Asia/Seoul
    username: app
    password: app1234
  jpa:
    hibernate:
      ddl-auto: update        # 초기엔 create / create-drop 사용 후 update로 변경 권장
    properties:
      hibernate:
        format_sql: true
    show-sql: true
```

> 컨테이너까지 함께 띄우는 경우에는 URL을 `jdbc:mysql://mysql:3306/myapp` 로 설정하면
> 같은 Docker 네트워크에서 서비스 이름(`mysql`)으로 접근할 수 있습니다.

---

## 5) JPA로 테이블 생성 (옵션 A)
1. 최소 1개 이상의 `@Entity` 작성 및 리포지토리 추가
2. 애플리케이션 실행
3. 콘솔에서 Hibernate DDL 로그 확인
4. 생성 확인
   ```bash
   docker exec -it hh_mysql mysql -uapp -papp1234 myapp -e "SHOW TABLES;"
   ```

---

## 6) 기존 데이터 가져오기 (옵션 B)
덤프 파일이 있다면 다음으로 복원합니다.

```bash
# 1) 덤프 파일을 컨테이너로 복사
docker cp C:ackup\myapp.sql hh_mysql:/tmp/myapp.sql

# 2) 컨테이너 내부에서 복원
docker exec -it hh_mysql bash -lc "mysql -uroot -proot1234 myapp < /tmp/myapp.sql"
```

---

## 7) 자주 쓰는 명령어
```bash
docker ps
docker logs -f hh_mysql
docker compose restart mysql
docker compose down         # 컨테이너 중지/삭제
docker compose down -v      # 볼륨까지 삭제(초기화)
```

---

## 8) 문제 해결 체크리스트
- 포트 충돌 없이 **3307**이 열려 있는지 (`docker ps`에서 `0.0.0.0:3307->3306/tcp` 확인)
- JDBC URL이 `127.0.0.1:3307/myapp`인지 (로컬 IDE 실행 기준)
- 권한/계정이 맞는지 (`app/app1234`)
- 엔티티 패키지가 `@SpringBootApplication` 하위에 있어 JPA 스캔이 되는지
- 로그가 보고 싶으면 `show-sql: true`와 `hibernate.format_sql` 활성화

---

## 9) 결과
이 가이드를 완료하면, **로컬 MySQL을 중지하지 않고도** Docker MySQL(3307)을 사용해
Spring Boot 애플리케이션을 안정적으로 개발/테스트할 수 있습니다.
