FROM eclipse-temurin:21-jre-alpine AS builder
WORKDIR /app
COPY target/xr-tech-api.jar app.jar

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# JVM调优参数
ENV JAVA_OPTS="\
  -Xms512m \
  -Xmx1024m \
  -Xmn256m \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+UseCompressedOops \
  -XX:+UnlockCommercialFeatures \
  -XX:+FlightRecorder \
  -XX:StartFlightRecording=delay=60s,duration=300s,filename=/tmp/jfrdump.jfr,settings=profile \
  -Xlog:gc*:file=/var/log/gc.log:time:filecount=5,filesize=10M"

# 堆转储路径
ENV HEAP_DUMP_PATH="/var/log/heapdump.hprof"

# 启用JMX监控（仅开发环境）
ENV JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote.port=9010 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"

COPY --from=builder /app/app.jar app.jar
COPY --from=builder /app/logs /var/log

EXPOSE 8080 9010

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]