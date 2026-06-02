# 编译并运行项目
cd <项目根目录>
./mvnw spring-boot:run

# 或打包后运行
./mvnw clean package -DskipTests
java -jar target/hikari-demo-1.0.0.jar

# 启动后访问Actuator端点查看连接池状态
curl http://localhost:8080/actuator/metrics/hikaricp.connections.active

# 查看详细连接池信息
curl http://localhost:8080/actuator/metrics/hikaricp