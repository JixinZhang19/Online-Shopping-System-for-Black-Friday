CREATE TABLE promotion_log(
    order_number        BIGINT              NOT NULL,
    user_id             VARCHAR(36)         NOT NULL,
    promotion_id        VARCHAR(36)         NOT NULL,
    operation_name      VARCHAR(36)         NOT NULL,
    create_time         TIMESTAMP           NOT NULL,
    PRIMARY KEY(order_number, operation_name)
) ENGINE = InnoDB   CHARSET = utf8mb4;

CREATE INDEX idx_user_id ON promotion_log(user_id);
CREATE INDEX idx_order_number ON promotion_log(order_number);