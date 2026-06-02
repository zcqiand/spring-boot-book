-- 用户表
CREATE TABLE sys_user (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    username        VARCHAR(50) NOT NULL UNIQUE COMMENT '登录用户名',
    password        VARCHAR(255) NOT NULL COMMENT '加密后的密码',
    email           VARCHAR(100) COMMENT '邮箱地址',
    mobile          VARCHAR(20) COMMENT '手机号',
    enabled         BOOLEAN DEFAULT TRUE COMMENT '账号是否启用',
    account_non_locked BOOLEAN DEFAULT TRUE COMMENT '是否锁定',
    credentials_non_expired BOOLEAN DEFAULT TRUE COMMENT '凭证是否过期',
    account_non_expired BOOLEAN DEFAULT TRUE COMMENT '账号是否过期',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login_at   TIMESTAMP COMMENT '最后登录时间',
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 角色表
CREATE TABLE sys_role (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_code       VARCHAR(50) NOT NULL UNIQUE COMMENT '角色代码：ADMIN/MANAGER/TECHNICIAN/RESEARCHER',
    role_name       VARCHAR(100) NOT NULL COMMENT '角色名称',
    description     VARCHAR(255) COMMENT '角色描述',
    parent_id       BIGINT COMMENT '父角色ID，支持角色继承',
    role_sort       INT DEFAULT 0 COMMENT '角色排序',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES sys_role(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- 权限表
CREATE TABLE sys_permission (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    perm_code       VARCHAR(100) NOT NULL UNIQUE COMMENT '权限代码：system:user:read',
    perm_name       VARCHAR(100) NOT NULL COMMENT '权限名称',
    resource_type   VARCHAR(20) NOT NULL COMMENT '资源类型：menu/button/api/data',
    resource_path   VARCHAR(255) COMMENT '资源路径',
    action          VARCHAR(20) COMMENT '操作类型：read/write/delete/execute',
    data_scope      VARCHAR(20) DEFAULT 'own' COMMENT '数据范围：all/dept/lab/own',
    description     VARCHAR(255) COMMENT '权限描述',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_perm_code (perm_code),
    INDEX idx_resource_type (resource_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统权限表';

-- 用户角色关联表（多对多）
CREATE TABLE sys_user_role (
    user_id         BIGINT NOT NULL,
    role_id         BIGINT NOT NULL,
    assigned_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    assigned_by     BIGINT COMMENT '授权人ID',
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES sys_role(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 角色权限关联表（多对多）
CREATE TABLE sys_role_permission (
    role_id         BIGINT NOT NULL,
    permission_id   BIGINT NOT NULL,
    assigned_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES sys_role(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES sys_permission(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 用户实验室关联表（用户可属于多个实验室）
CREATE TABLE sys_user_lab (
    user_id         BIGINT NOT NULL,
    lab_id          BIGINT NOT NULL,
    is_primary      BOOLEAN DEFAULT FALSE COMMENT '是否为主实验室',
    joined_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, lab_id),
    FOREIGN KEY (user_id) REFERENCES sys_user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户实验室关联表';

-- 初始化数据
INSERT INTO sys_role (role_code, role_name, description) VALUES
('ADMIN', '系统管理员', '拥有系统全部权限'),
('MANAGER', '实验室主任', '管理实验室日常运营'),
('TECHNICIAN', '实验技术员', '执行实验操作'),
('RESEARCHER', '研究员', '进行科研工作');

INSERT INTO sys_permission (perm_code, perm_name, resource_type, action, data_scope) VALUES
('system:user:read', '查看用户', 'api', 'read', 'all'),
('system:user:write', '管理用户', 'api', 'write', 'dept'),
('system:experiment:read', '查看实验', 'api', 'read', 'own'),
('system:experiment:write', '管理实验', 'api', 'write', 'own'),
('system:experiment:delete', '删除实验', 'api', 'delete', 'own'),
('system:device:read', '查看设备', 'api', 'read', 'lab'),
('system:device:write', '管理设备', 'api', 'write', 'lab'),
('system:report:read', '查看报告', 'api', 'read', 'own'),
('system:report:export', '导出报告', 'api', 'execute', 'dept');