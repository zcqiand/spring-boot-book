# 查看所有可用端点（需要先开放）
curl http://localhost:8080/actuator

# 健康检查（默认开放）
curl http://localhost:8080/actuator/health

# 带详情健康检查
curl http://localhost:8080/actuator/health?showDetails=always

# 应用信息
curl http://localhost:8080/actuator/info

# 查看所有Bean（需要先开放）
curl http://localhost:8080/actuator/beans

# 查看环境变量（需要先开放）
curl http://localhost:8080/actuator/env

# 查看指标
curl http://localhost:8080/actuator/metrics

# 查看特定指标（如JVM堆内存）
curl http://localhost:8080/actuator/metrics/jvm.memory.used