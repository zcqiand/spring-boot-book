# 关系型数据权限的查询示例
def query_accessible_records(user: User, record_type: str):
    """查询用户有权限访问的记录"""
    # 获取用户参与的所有项目
    project_ids = get_user_project_ids(user.id)

    # 根据记录类型构建查询
    if record_type == 'experiment':
        return db.query(ExperimentRecord).filter(
            ExperimentRecord.project_id.in_(project_ids)
        )
    elif record_type == 'device':
        # 设备权限可能基于实验室
        lab_ids = get_user_lab_ids(user.id)
        return db.query(Device).filter(Device.lab_id.in_(lab_ids))