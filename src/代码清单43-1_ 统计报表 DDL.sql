-- 每日统计数据表（用于趋势分析）
CREATE TABLE daily_statistics (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    stat_date       DATE NOT NULL COMMENT '统计日期',
    category        VARCHAR(50) NOT NULL COMMENT '指标类别',
    metric_name     VARCHAR(100) NOT NULL COMMENT '指标名称',
    metric_value    DECIMAL(12,2) COMMENT '指标值',
    dimension       VARCHAR(50) COMMENT '维度：lab_id/equipment_id/user_id',
    dimension_value VARCHAR(100) COMMENT '维度值',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_date_metric_dimension (stat_date, category, metric_name, dimension, dimension_value)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='每日统计数据表';

-- 设备使用记录表（用于设备利用率统计）
CREATE TABLE equipment_usage_log (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    equipment_id    BIGINT NOT NULL COMMENT '设备ID',
    start_time      TIMESTAMP NOT NULL COMMENT '开始时间',
    end_time        TIMESTAMP COMMENT '结束时间（null表示正在使用）',
    task_id         BIGINT COMMENT '关联任务ID',
    operator_id     BIGINT COMMENT '操作人ID',
    usage_type      VARCHAR(20) COMMENT '使用类型：INSPECTION/CALIBRATION/MAINTENANCE',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usage_equipment FOREIGN KEY (equipment_id) REFERENCES equipment(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备使用记录表';