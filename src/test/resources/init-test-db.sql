-- user 테이블
CREATE TABLE `user` (
    `user_key` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `user_id` VARCHAR(255) NOT NULL COLLATE 'utf8mb4_unicode_ci',
    `user_name` VARCHAR(255) NOT NULL COLLATE 'utf8mb4_unicode_ci',
    `phone` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_unicode_ci',
    `email` VARCHAR(255) NOT NULL COLLATE 'utf8mb4_unicode_ci',
    `point` INT(11) NOT NULL,
    `has_coupon` ENUM('Y','N') NOT NULL COLLATE 'utf8mb4_unicode_ci',
    `use_state` ENUM('Y','N') NOT NULL COLLATE 'utf8mb4_unicode_ci',
    `regist_date` DATETIME(6) NULL DEFAULT NULL,
    `modi_date` DATETIME(6) NULL DEFAULT NULL,
    `delete_date` DATETIME(6) NULL DEFAULT NULL,
    PRIMARY KEY (`user_key`) USING BTREE
) COLLATE='utf8mb4_unicode_ci' ENGINE=InnoDB AUTO_INCREMENT=1;

-- product 테이블
CREATE TABLE `product` (
    `product_key` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `product_code` VARCHAR(36) NOT NULL COLLATE 'utf8mb4_unicode_ci',
    `product_name` VARCHAR(100) NOT NULL COLLATE 'utf8mb4_unicode_ci',
    `price` INT(11) NOT NULL,
    `amount` INT(11) NOT NULL,
    `product_info` TEXT NULL DEFAULT NULL COLLATE 'utf8mb4_unicode_ci',
    PRIMARY KEY (`product_key`) USING BTREE,
    UNIQUE INDEX `product_code` (`product_code`) USING BTREE,
    INDEX `idx_price` (`price`) USING BTREE,
    INDEX `idx_amount_product_code` (`amount`, `product_code`) USING BTREE
) COLLATE='utf8mb4_unicode_ci' ENGINE=InnoDB AUTO_INCREMENT=1;

-- orders 테이블
CREATE TABLE `orders` (
    `order_key` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `order_id` VARCHAR(255) NOT NULL COLLATE 'utf8mb4_unicode_ci',
    `user_key` BIGINT(20) NOT NULL,
    `total_amount` INT(11) NOT NULL,
    `order_status` ENUM('COMPLETED','CANCELLED','DELIVERING') NOT NULL COLLATE 'utf8mb4_unicode_ci',
    PRIMARY KEY (`order_key`) USING BTREE,
    UNIQUE INDEX `UK_hmsk25beh6atojvle1xuymjj0` (`order_id`) USING BTREE,
    INDEX `FKiw0pcbnm1w5ubtf4r2kggx31o` (`user_key`) USING BTREE,
    CONSTRAINT `FKiw0pcbnm1w5ubtf4r2kggx31o` FOREIGN KEY (`user_key`) REFERENCES `user` (`user_key`) ON UPDATE RESTRICT ON DELETE RESTRICT
) COLLATE='utf8mb4_unicode_ci' ENGINE=InnoDB AUTO_INCREMENT=1;

-- order_item 테이블
CREATE TABLE `order_item` (
    `order_item_key` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `order_key` BIGINT(20) NOT NULL,
    `order_item_id` VARCHAR(255) NOT NULL COLLATE 'utf8mb4_unicode_ci',
    `product_key` BIGINT(20) NOT NULL,
    `price_at_order` INT(11) NOT NULL,
    `quantity` INT(11) NOT NULL,
    PRIMARY KEY (`order_item_key`) USING BTREE,
    UNIQUE INDEX `UK_6mlhinttjwgluhuybap6lrfvq` (`order_item_id`) USING BTREE,
    INDEX `FKdg0q69c8fmrwr6kxuenrfd9t2` (`order_key`) USING BTREE,
    CONSTRAINT `FKdg0q69c8fmrwr6kxuenrfd9t2` FOREIGN KEY (`order_key`) REFERENCES `orders` (`order_key`) ON UPDATE RESTRICT ON DELETE RESTRICT
) COLLATE='utf8mb4_unicode_ci' ENGINE=InnoDB AUTO_INCREMENT=1;

-- 테스트용 데이터 삽입
INSERT INTO `user` (`user_id`, `user_name`, `phone`, `email`, `point`, `has_coupon`, `use_state`) 
VALUES ('testuser', 'Tester', '010-1234-5678', 'test@example.com', 100000, 'N', 'Y');

INSERT INTO `product` (`product_code`, `product_name`, `price`, `amount`, `product_info`) 
VALUES ('P001', 'Sample Product', 10000, 10, 'Test Info'),
       ('P002', '테스트상품_02', 15000, 10, '두번째 상품 정보');