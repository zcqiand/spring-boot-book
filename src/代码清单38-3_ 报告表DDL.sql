CREATE TABLE `report` (
    `report_id` VARCHAR(36) NOT NULL COMMENT '报告ID(UUID)',
    `report_code` VARCHAR(50) NOT NULL COMMENT '报告编号',
    `title` VARCHAR(200) NOT NULL COMMENT '报告标题',
    `exp_id` VARCHAR(36) NOT NULL COMMENT '关联实验ID',
    `creator_id` VARCHAR(36) NOT NULL COMMENT '创建人ID',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态:0=草稿,1=待审批,2=已批准,3=已归档',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (`report_id`),
    UNIQUE KEY `uk_report_code` (`report_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报告表';