# 查看整体健康状态
curl http://localhost:8080/actuator/health

# 查看数据库健康指示器详情
curl http://localhost:8080/actuator/health/dbConnectionHealth

# 查看业务服务健康指示器详情
curl http://localhost:8080/actuator/health/serviceHealth

# 查看所有可用健康指示器
curl http://localhost:8080/actuator/health

# 查看metrics端点
curl http://localhost:8080/actuator/metrics

# 查看特定指标
curl http://localhost:8080/actuator/metrics/jvm.memory.used