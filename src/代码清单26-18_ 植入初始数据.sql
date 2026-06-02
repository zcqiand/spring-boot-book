-- 文件：src/main/resources/db/migration/V3__seed_initial_data.sql
INSERT INTO t_role (role_name, role_code, description) VALUES
    ('系统管理员', 'ADMIN', '拥有所有系统权限'),
    ('运维人员', 'OP', '拥有运维相关权限'),
    ('普通用户', 'USER', '拥有基本访问权限');

INSERT INTO t_user (username, password, email) VALUES
    ('admin', '$2a$10$xxx', 'admin@example.com');