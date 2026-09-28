FROM eclipse-temurin:21-jre-jammy AS runtime
WORKDIR /app

# slim 缺 wget —— Docker HEALTHCHECK 需要它探 /actuator/health
RUN apt-get update \
 && apt-get install -y --no-install-recommends wget ca-certificates \
 && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/app.jar /app/app.jar

# JVM 在容器内堆上限参考 cgroup 内存限额（默认 75%）
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0"
ENV SERVER_PORT=5105
ENV TZ=UTC

EXPOSE 5105

# Bean wiring tolerance: Spring Boot 冷启动 5-15s @ 小 VPS。
# probe 走 /actuator/health（spring-boot-starter-actuator + management.endpoint.health.probes.enabled:true
# 在 application.yml 里开）。如果 servlet 链还没就绪, /actuator/health 会返回 503,
# Docker HEALTHCHECK exit 1, container restart-loop —— 这是想要的 fail-loud 行为。
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD wget -q --spider http://127.0.0.1:5105/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]