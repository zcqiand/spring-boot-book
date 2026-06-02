-- 设备表
CREATE TABLE equipment (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    equipment_no    VARCHAR(50) NOT NULL UNIQUE COMMENT '设备编号',
    name            VARCHAR(200) NOT NULL COMMENT '设备名称',
    model           VARCHAR(100) COMMENT '型号',
    manufacturer    VARCHAR(100) COMMENT '制造商',
    serial_no       VARCHAR(100) COMMENT '序列号',
    lab_id          BIGINT NOT NULL COMMENT '所属实验室',
    category        VARCHAR(50) COMMENT '设备类别：力学/电学/化学/光学',
    status          VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE/MAINTENANCE/SCRAPPED',
    purchase_date   DATE COMMENT '购置日期',
    purchase_price  DECIMAL(12,2) COMMENT '购置价格',
    asset_no        VARCHAR(50) COMMENT '资产编号',
    photo_url       VARCHAR(500) COMMENT '设备照片URL',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_equipment_no (equipment_no),
    INDEX idx_lab (lab_id),
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备表';

-- 设备校准记录表
CREATE TABLE equipment_calibration (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    equipment_id    BIGINT NOT NULL COMMENT '设备ID',
    calibration_date DATE NOT NULL COMMENT '校准日期',
    next_date       DATE COMMENT '下次校准日期（提前预警用）',
    result          VARCHAR(20) COMMENT '校准结果：PASS/FAIL',
    certificate_no  VARCHAR(100) COMMENT '校准证书编号',
    calibrator      VARCHAR(100) COMMENT '校准机构',
    remark          TEXT COMMENT '备注',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cal_equipment FOREIGN KEY (equipment_id) REFERENCES equipment(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备校准记录表';

-- 设备维护记录表
CREATE TABLE equipment_maintenance (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    equipment_id    BIGINT NOT NULL COMMENT '设备ID',
    maintenance_date DATE NOT NULL COMMENT '维护日期',
    maintenance_type VARCHAR(50) COMMENT '维护类型：ROUTINE/BREAKDOWN/PREVENTIVE',
    description     TEXT COMMENT '维护内容',
    cost            DECIMAL(12,2) COMMENT '维护费用',
    operator_id     BIGINT COMMENT '维护人',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_maint_equipment FOREIGN KEY (equipment_id) REFERENCES equipment(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备维护记录表';