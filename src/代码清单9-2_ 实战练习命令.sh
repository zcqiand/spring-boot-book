# 启动应用
./mvnw spring-boot:run

# 验证健康检查端点可用
curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/actuator/health
# 期望输出: 200

# 验证K8s探针端点可用
curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/actuator/health/liveness
# 期望输出: 200

# 验证敏感端点已关闭
curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/actuator/beans
# 期望输出: 403

# 验证shutdown端点已关闭
curl -s -o /dev/null -w "%{http_code}" -X POST http://localhost:8080/actuator/shutdown
# 期望输出: 403