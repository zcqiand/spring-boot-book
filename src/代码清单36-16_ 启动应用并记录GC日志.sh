# 创建日志目录
mkdir -p logs

# 使用调优参数启动应用
java \
  -Xms256m \
  -Xmx512m \
  -Xmn128m \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=100 \
  -XX:+UseCompressedOops \
  -XX:+UnlockCommercialFeatures \
  -XX:+FlightRecorder \
  -Xlog:gc*:file=logs/gc.log:time:filecount=5,filesize=10M \
  -jar target/jvm-exercise-1.0.0.jar

# 后台运行方式
nohup java \
  -Xms256m -Xmx512m -Xmn128m \
  -XX:+UseG1GC -XX:MaxGCPauseMillis=100 \
  -XX:+UseCompressedOops \
  -XX:+UnlockCommercialFeatures -XX:+FlightRecorder \
  -Xlog:gc*:file=logs/gc.log:time \
  -jar target/jvm-exercise-1.0.0.jar \
  > logs/app.log 2>&1 &

echo $! > app.pid
echo "应用PID: $(cat app.pid)"