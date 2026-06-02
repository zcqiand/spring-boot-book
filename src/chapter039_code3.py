# 静态数据范围的 SQL 注入示例
def get_data_filter(user: User, resource: str) -> dict:
    """根据用户的数据权限范围生成过滤条件"""
    if user.scope == 'all':
        return {}  # 无限制
    elif user.scope == 'lab':
        return {'lab_id': user.lab_id}
    elif user.scope == 'own':
        return {'creator_id': user.id}
    else:
        return {}