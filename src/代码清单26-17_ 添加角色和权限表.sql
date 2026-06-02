-- 文件：src/main/resources/db/migration/V2__add_role_tables.sql
CREATE TABLE t_role (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name       VARCHAR(50) NOT NULL UNIQUE,
    role_code       VARCHAR(50) NOT NULL UNIQUE,
    description     VARCHAR(200),
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE t_user_role (
    user_id         BIGINT NOT NULL,
    role_id         BIGINT NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES t_user(id),
    CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES t_role(id)
);