FROM eclipse-temurin:21-jre-jammy AS runtime
WORKDIR /app
# 中略，见源文件

RUN apt-get update \
 && apt-get install -y --no-install-recommends wget ca-certificates \
 && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/app.jar /app/app.jar
# 中略，见源文件

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0"
ENV SERVER_PORT=5205
ENV TZ=UTC

EXPOSE 5205
# 中略：actuator 探针注释，见源文件

HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD wget -q --spider http://127.0.0.1:5205/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]