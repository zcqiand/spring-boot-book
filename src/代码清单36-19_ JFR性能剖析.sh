PID=$(cat app.pid)

# 启动60秒性能剖析
jcmd $PID JFR.start name=exercise recording=profile delay=5s duration=60s

# 检查状态
jcmd $PID JFR.check

# 模拟业务请求
for i in {1..10}; do
  curl -X GET "http://localhost:8080/api/memory/allocate/10"
  sleep 2
done

# 等待录制完成
sleep 30

# 转储录制文件
jcmd $PID JFR.dump name=exercise filename=/tmp/exercise.jfr

# 停止录制
jcmd $PID JFR.stop name=exercise

echo "录制文件保存在: /tmp/exercise.jfr"