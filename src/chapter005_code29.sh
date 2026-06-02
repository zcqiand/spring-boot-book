# 编译项目
./mvnw compile

# 运行演示程序
./mvnw spring-boot:run -DskipTests

# 或直接运行主类
./mvnw exec:java -Dexec.mainClass="com.example.demo.DiDemoApplication"