FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 复制jar包（假设已构建）
COPY target/*.jar app.jar

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8080

ENTRYPOINT ["java", "$JAVA_OPTS", "-jar", "app.jar"]