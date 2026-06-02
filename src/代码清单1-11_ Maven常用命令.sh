# 1. 创建 Spring Boot 项目
spring init --name=demo --dependencies=web demo

# 2. 启动项目
./mvnw spring-boot:run

# 3. 打包跳过测试
./mvnw package -DskipTests

# 4. 运行打包后的 jar
java -jar target/demo-0.0.1-SNAPSHOT.jar

# 5. 查看依赖树
./mvnw dependency:tree

# 6. 清理并重新构建
./mvnw clean package -DskipTests