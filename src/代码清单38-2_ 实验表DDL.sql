CREATE TABLE `experiment` (
    `exp_id` VARCHAR(36) NOT NULL COMMENT '实验ID(UUID)',
    `exp_code` VARCHAR(50) NOT NULL COMMENT '实验编号',
    `exp_name` VARCHAR(200) NOT NULL COMMENT '实验名称',
    `requester_id` VARCHAR(36) NOT NULL COMMENT '申请人ID',
    `assigned_to` VARCHAR(36) DEFAULT NULL COMMENT '指派人ID',
    `equip_id` VARCHAR(36) DEFAULT NULL COMMENT '使用设备ID',
    `start_time` DATETIME NOT NULL COMMENT '计划开始时间',
    `end_time` DATETIME NOT NULL COMMENT '计划结束时间',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态:0=待分配,1=已分配,2=进行中,3=已完成',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (`exp_id`),
    UNIQUE KEY `uk_exp_code` (`exp_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='实验表';