FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# 缓存友好的层：先只复制 pom.xml 跑 dependency:go-offline，
# 再 copy src。多数 commit 只动 src, deps 缓存命中。
COPY pom.xml ./
RUN mvn -B -e -ntp -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -B -e -ntp -DskipTests package \
 && cp target/platform-0.1.0.jar /app/app.jar