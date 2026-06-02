# 启动应用
mvn spring-boot:run

# 查看依赖树
mvn dependency:tree

# 查看特定依赖的传递路径
mvn dependency:tree -Dincludes=org.springframework:*

# 健康检查
curl http://localhost:8080/actuator/health