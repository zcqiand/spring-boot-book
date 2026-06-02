-- 文件：src/main/resources/db/migration/V1__init_user_tables.sql
CREATE TABLE t_user (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    username        VARCHAR(50)  NOT NULL UNIQUE,
    password        VARCHAR(128) NOT NULL,
    email           VARCHAR(100),
    status          TINYINT DEFAULT 1,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE t_session (
    id              VARCHAR(36) PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    token           VARCHAR(256) NOT NULL,
    expires_at      TIMESTAMP NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_session_user FOREIGN KEY (user_id) REFERENCES t_user(id)
);