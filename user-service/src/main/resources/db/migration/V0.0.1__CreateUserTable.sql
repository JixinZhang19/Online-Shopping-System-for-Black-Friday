CREATE TABLE user (
    user_id     varchar(36)                 not null,
    user_name   varchar(100)    BINARY      not null    unique,
    password    varchar(36)     BINARY      not null,
    primary key(user_id)
) ENGINE = InnoDB CHARSET=utf8;

INSERT INTO user(user_id, user_name, password) VALUE ("1", "zjx", "test");