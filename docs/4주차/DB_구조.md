```mermaid
erDiagram

    user {
        BIGINT user_key PK "AUTO_INCREMENT"
        VARCHAR user_id "UUID 형식, UNIQUE"
        VARCHAR user_name
        VARCHAR phone
        VARCHAR email UNIQUE
        ENUM use_state "Y/N"
        ENUM has_coupon "Y/N"
        DATETIME regist_date
        DATETIME modi_date
        DATETIME delete_date
    }

    coupon {
        BIGINT coupon_key PK "AUTO_INCREMENT"
        VARCHAR coup_id UNIQUE
        VARCHAR coup_name
        TEXT coup_desc
        INT discount_amount
        INT total_count
        DATETIME regist_date
        DATE start_date
        DATE end_date
    }

    user_coupon {
        BIGINT user_coupon_key PK "AUTO_INCREMENT"
        VARCHAR user_id FK
        BIGINT coupon_key FK
        ENUM is_used "USED/UNUSED"
    }

    order {
        BIGINT order_key PK "AUTO_INCREMENT"
        VARCHAR order_id UNIQUE
        VARCHAR user_id FK
        DATETIME order_date
        ENUM order_status "COMPLETED/CANCELLED/DELIVERING"
        INT total_amount
    }

    product {
        BIGINT product_key PK "AUTO_INCREMENT"
        VARCHAR product_code UNIQUE
        VARCHAR product_name
        TEXT product_info
        INT price
        INT amount
    }

    order_item {
        BIGINT order_item_key PK "AUTO_INCREMENT"
        VARCHAR order_item_id UNIQUE
        BIGINT order_key FK
        BIGINT product_key FK
        INT quantity
        INT price_at_order
    }

    payment {
        BIGINT payment_key PK "AUTO_INCREMENT"
        VARCHAR payment_id UNIQUE
        BIGINT order_key FK
        ENUM payment_method "CARD/BANK/PAY"
        ENUM payment_status "PAID/FAILED/REFUNDED"
        DATETIME paid_at
        INT paid_amount
    }

    order_history {
        BIGINT order_history_key PK "AUTO_INCREMENT"
        VARCHAR history_id UNIQUE
        VARCHAR user_id FK
        BIGINT order_key FK
        VARCHAR status
        DATETIME changed_at
    }

    user ||--o{ user_coupon : has
    coupon ||--o{ user_coupon : given
    user ||--o{ order : places
    order ||--o{ order_item : includes
    product ||--o{ order_item : includes
    order ||--|| payment : pays
    user ||--o{ order_history : logs
    order ||--o{ order_history : tracks
```
