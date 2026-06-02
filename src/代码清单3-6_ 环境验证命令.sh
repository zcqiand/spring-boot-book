# 验证项目是否能正常编译
./mvnw clean compile

# 验证项目是否能正常启动
./mvnw spring-boot:run

# 验证依赖是否完整
./mvnw dependency:tree