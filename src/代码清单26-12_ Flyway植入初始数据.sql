-- 文件：db/migration/V2__seed_initial_data.sql
INSERT INTO t_role (role_name, description) VALUES
    ('ADMIN', '系统管理员'),
    ('USER', '普通用户'),
    ('GUEST', '访客');

INSERT INTO t_user (username, password, email) VALUES
    ('admin', '$2a$10$xxx', 'admin@example.com');