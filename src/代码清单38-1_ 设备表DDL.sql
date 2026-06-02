CREATE TABLE `equipment` (
    `equip_id` VARCHAR(36) NOT NULL COMMENT '设备ID(UUID)',
    `equip_code` VARCHAR(50) NOT NULL COMMENT '设备编号',
    `equip_name` VARCHAR(100) NOT NULL COMMENT '设备名称',
    `category` VARCHAR(50) NOT NULL COMMENT '设备类别',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态:0=报废,1=正常,2=维修中',
    `responsible_person` VARCHAR(36) DEFAULT NULL COMMENT '负责人ID',
    `purchase_date` DATE DEFAULT NULL COMMENT '采购日期',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (`equip_id`),
    UNIQUE KEY `uk_equip_code` (`equip_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备表';