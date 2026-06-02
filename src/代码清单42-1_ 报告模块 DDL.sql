-- 报告表
CREATE TABLE inspection_report (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    report_no       VARCHAR(50) NOT NULL UNIQUE COMMENT '报告编号',
    task_id         BIGINT NOT NULL COMMENT '关联任务ID',
    title           VARCHAR(200) COMMENT '报告标题',
    status          VARCHAR(20) NOT NULL DEFAULT 'DRAFT' COMMENT '状态：DRAFT/PENDING_REVIEW/APPROVED/PUBLISHED',
    template_id     BIGINT COMMENT '模板ID',
    content         TEXT COMMENT '报告内容（HTML/PDF）',
    pdf_url         VARCHAR(500) COMMENT 'PDF文件URL',
    created_by      BIGINT NOT NULL COMMENT '编制人',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    published_at    TIMESTAMP COMMENT '发布时间',
    INDEX idx_report_no (report_no),
    INDEX idx_task (task_id),
    INDEX idx_status (status),
    CONSTRAINT fk_report_task FOREIGN KEY (task_id) REFERENCES inspection_task(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='检测报告表';

-- 报告审批记录表
CREATE TABLE report_approval_log (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    report_id       BIGINT NOT NULL COMMENT '报告ID',
    approval_type   VARCHAR(20) NOT NULL COMMENT '审批类型：REVIEW/APPROVE/SIGN',
    approver_id     BIGINT NOT NULL COMMENT '审批人ID',
    result          VARCHAR(20) NOT NULL COMMENT '审批结果：APPROVED/REJECTED',
    comment         TEXT COMMENT '审批意见',
    signed_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_approval_report FOREIGN KEY (report_id) REFERENCES inspection_report(id) ON DELETE CASCADE,
    CONSTRAINT fk_approval_user FOREIGN KEY (approver_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报告审批记录表';

-- 报告模板表
CREATE TABLE report_template (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(100) NOT NULL COMMENT '模板名称',
    template_type   VARCHAR(50) COMMENT '模板类型：MATERIAL/STRUCTURE/ENVIRONMENT',
    content         TEXT COMMENT '模板内容（JSON格式）',
    variables       TEXT COMMENT '变量定义JSON',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报告模板表';