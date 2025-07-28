CREATE TABLE user (
user_key     BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT 'AUTO_INCREMENT 사용자 PK',
user_id      VARCHAR(36) NOT NULL UNIQUE COMMENT '사용자 고유 ID (UUID 등)',
user_name    VARCHAR(50) NOT NULL COMMENT '사용자 이름',
phone        VARCHAR(20) NOT NULL COMMENT '연락처',
email        VARCHAR(100) NOT NULL UNIQUE COMMENT '이메일 주소',
use_state    ENUM('Y', 'N') DEFAULT 'Y' COMMENT '활성/비활성 상태',
has_coupon   ENUM('Y', 'N') DEFAULT 'N' COMMENT '쿠폰 보유 여부',
regist_date  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '가입 일자',
modi_date    DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일자',
delete_date  DATETIME DEFAULT NULL COMMENT '삭제 처리 일자'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='회원';


CREATE TABLE coupon (
coupon_key      BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT 'AUTO_INCREMENT 쿠폰 PK',
coup_id         VARCHAR(36) NOT NULL UNIQUE COMMENT '쿠폰 고유 ID',
coup_name       VARCHAR(100) NOT NULL COMMENT '쿠폰 이름',
coup_desc       TEXT COMMENT '쿠폰 설명',
discount_amount INT NOT NULL COMMENT '할인 금액',
total_count     INT NOT NULL COMMENT '전체 발급 수량',
regist_date     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 일자',
start_date      DATE NOT NULL COMMENT '사용 시작일',
end_date        DATE NOT NULL COMMENT '사용 종료일'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='쿠폰';


CREATE TABLE user_coupon (
user_coupon_key BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT 'AUTO_INCREMENT 사용자 쿠폰 PK',
user_id         VARCHAR(36) NOT NULL COMMENT '사용자 ID',
coupon_key      BIGINT UNSIGNED NOT NULL COMMENT '쿠폰 PK',
is_used         ENUM('USED', 'UNUSED') DEFAULT 'UNUSED' COMMENT '사용 여부',
UNIQUE KEY uq_user_coupon (user_id, coupon_key),
FOREIGN KEY (user_id) REFERENCES user(user_id)
ON DELETE RESTRICT ON UPDATE CASCADE,
FOREIGN KEY (coupon_key) REFERENCES coupon(coupon_key)
ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='사용자 쿠폰 매핑';


CREATE TABLE `order` (
order_key     BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT 'AUTO_INCREMENT 주문 PK',
order_id      VARCHAR(36) NOT NULL UNIQUE COMMENT '주문 고유 ID',
user_id       VARCHAR(36) NOT NULL COMMENT '주문자 ID',
order_date    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '주문 일자',
order_status  ENUM('COMPLETED', 'CANCELLED', 'DELIVERING') DEFAULT 'COMPLETED' COMMENT '주문 상태',
total_amount  INT NOT NULL COMMENT '총 주문 금액',
FOREIGN KEY (user_id) REFERENCES user(user_id)
ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='주문';


CREATE TABLE product (
product_key   BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT 'AUTO_INCREMENT 상품 PK',
product_code  VARCHAR(36) NOT NULL UNIQUE COMMENT '상품 고유 코드',
product_name  VARCHAR(100) NOT NULL COMMENT '상품명',
product_info  TEXT COMMENT '상품 상세 설명',
price         INT NOT NULL COMMENT '정가',
amount        INT NOT NULL COMMENT '재고 수량'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='상품';



CREATE TABLE order_item (
order_item_key BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT 'AUTO_INCREMENT 주문 항목 PK',
order_item_id  VARCHAR(36) NOT NULL UNIQUE COMMENT '주문 항목 고유 ID',
order_key      BIGINT UNSIGNED NOT NULL COMMENT '주문 PK',
product_key    BIGINT UNSIGNED NOT NULL COMMENT '상품 PK',
quantity       INT NOT NULL COMMENT '주문 수량',
price_at_order INT NOT NULL COMMENT '주문 시 가격',
FOREIGN KEY (order_key) REFERENCES `order`(order_key)
ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY (product_key) REFERENCES product(product_key)
ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='주문 항목';


CREATE TABLE payment (
payment_key     BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT 'AUTO_INCREMENT 결제 PK',
payment_id      VARCHAR(36) NOT NULL UNIQUE COMMENT '결제 고유 ID',
order_key       BIGINT UNSIGNED NOT NULL COMMENT '주문 PK',
payment_method  ENUM('CARD', 'BANK', 'PAY') NOT NULL COMMENT '결제 방식',
payment_status  ENUM('PAID', 'FAILED', 'REFUNDED') NOT NULL COMMENT '결제 상태',
paid_at         DATETIME DEFAULT NULL COMMENT '결제 완료 시간',
paid_amount     INT NOT NULL COMMENT '실제 결제 금액',
FOREIGN KEY (order_key) REFERENCES `order`(order_key)
ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='결제';


CREATE TABLE order_history (
order_history_key BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT 'AUTO_INCREMENT 주문 이력 PK',
history_id        VARCHAR(36) NOT NULL UNIQUE COMMENT '이력 고유 ID',
user_id           VARCHAR(36) NOT NULL COMMENT '사용자 ID',
order_key         BIGINT UNSIGNED NOT NULL COMMENT '주문 PK',
status            VARCHAR(100) NOT NULL COMMENT '상태 내용',
changed_at        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '변경 시각',
FOREIGN KEY (user_id) REFERENCES user(user_id)
ON DELETE RESTRICT ON UPDATE CASCADE,
FOREIGN KEY (order_key) REFERENCES `order`(order_key)
ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='주문 상태 이력';


COMMIT;




-- USER
INSERT INTO user (user_id, user_name, phone, email)
VALUES ('u-001', '홍길동', '010-1234-5678', 'hong@example.com');

-- COUPON
INSERT INTO coupon (coup_id, coup_name, coup_desc, discount_amount, total_count, start_date, end_date)
VALUES ('c-001', 'WELCOME10', '신규 가입 10% 할인 쿠폰', 1000, 100, '2025-07-01', '2025-12-31');

-- USER_COUPONuser
INSERT INTO user_coupon (user_id, coupon_key, is_used)
VALUES ('u-001', 1, 'UNUSED');

-- PRODUCT
INSERT INTO product (product_code, product_name, product_info, price, amount)
VALUES ('p-001', '프리미엄 텀블러', '보온/보냉이 뛰어난 텀블러', 15000, 100);

-- ORDER
INSERT INTO `order` (order_id, user_id, total_amount)
VALUES ('o-001', 'u-001', 15000);

-- ORDER_ITEM
INSERT INTO order_item (order_item_id, order_key, product_key, quantity, price_at_order)
VALUES ('oi-001', 1, 1, 1, 15000);

-- PAYMENT
INSERT INTO payment (payment_id, order_key, payment_method, payment_status, paid_amount)
VALUES ('pay-001', 1, 'CARD', 'PAID', 15000);

-- ORDER_HISTORY
INSERT INTO order_history (history_id, user_id, order_key, status)
VALUES ('hist-001', 'u-001', 1, '주문 완료 → 배송 중');
