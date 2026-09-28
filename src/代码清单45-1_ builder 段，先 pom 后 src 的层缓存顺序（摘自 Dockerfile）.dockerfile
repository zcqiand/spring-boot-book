FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app
# 中略，见源文件

COPY pom.xml ./
RUN mvn -B -e -ntp -DskipTests dependency:go-offline

COPY src ./src
# 中略，见源文件
RUN mvn -B -e -ntp -DskipTests package \
 && cp target/lab-management-system-springboot-0.1.0-SNAPSHOT.jar /app/app.jar