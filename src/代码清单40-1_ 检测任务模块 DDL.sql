-- 检测任务表
CREATE TABLE inspection_task (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_no         VARCHAR(50) NOT NULL UNIQUE COMMENT '任务编号：INS-YYYYMMDD-XXXX',
    title           VARCHAR(200) NOT NULL COMMENT '任务标题',
    description     TEXT COMMENT '任务描述',
    status          VARCHAR(30) NOT NULL DEFAULT 'CREATED' COMMENT '任务状态',
    priority        VARCHAR(20) NOT NULL DEFAULT 'MEDIUM' COMMENT '优先级',
    assignee_id     BIGINT COMMENT '指派人ID',
    lab_id          BIGINT NOT NULL COMMENT '实验室ID',
    created_by      BIGINT NOT NULL COMMENT '创建人ID',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    due_date        DATE COMMENT '截止日期',
    completed_at    TIMESTAMP COMMENT '完成时间',
    rejection_reason TEXT COMMENT '驳回原因',
    INDEX idx_task_no (task_no),
    INDEX idx_status (status),
    INDEX idx_assignee (assignee_id),
    INDEX idx_lab (lab_id),
    CONSTRAINT fk_task_assignee FOREIGN KEY (assignee_id) REFERENCES sys_user(id),
    CONSTRAINT fk_task_lab FOREIGN KEY (lab_id) REFERENCES lab(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检测任务表';

-- 检测项目表（任务明细）
CREATE TABLE inspection_item (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_id         BIGINT NOT NULL COMMENT '所属任务ID',
    item_name       VARCHAR(200) NOT NULL COMMENT '检测项目名称',
    spec            VARCHAR(500) COMMENT '规格/标准',
    result          TEXT COMMENT '检测结果',
    is_passed       BOOLEAN COMMENT '是否合格：null-未检，true-合格，false-不合格',
    checked_by      BIGINT COMMENT '检测人ID',
    checked_at      TIMESTAMP COMMENT '检测时间',
    INDEX idx_item_task (task_id),
    CONSTRAINT fk_item_task FOREIGN KEY (task_id) REFERENCES inspection_task(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检测项目表';

-- 任务状态流转记录表
CREATE TABLE inspection_status_log (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_id         BIGINT NOT NULL COMMENT '任务ID',
    from_status     VARCHAR(30) COMMENT '原状态',
    to_status       VARCHAR(30) NOT NULL COMMENT '新状态',
    operator_id     BIGINT COMMENT '操作人ID',
    reason          VARCHAR(500) COMMENT '变更原因',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_log_task FOREIGN KEY (task_id) REFERENCES inspection_task(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务状态流转记录表';