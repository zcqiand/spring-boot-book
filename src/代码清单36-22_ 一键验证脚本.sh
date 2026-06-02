#!/bin/bash
set -e

echo "=== JVM调优验证脚本 ==="

# 检查应用是否运行
if [ ! -f app.pid ]; then
  echo "错误: app.pid不存在，请先启动应用"
  exit 1
fi

PID=$(cat app.pid)
echo "应用PID: $PID"

# 验证1: JVM参数
echo ""
echo "=== 验证1: JVM参数 ==="
jcmd $PID VM.flags | grep -E "(Xms|Xmx|Xmn|UseG1GC)" || true

# 验证2: GC统计
echo ""
echo "=== 验证2: GC统计 ==="
jstat -gcutil $PID

# 验证3: 线程状态
echo ""
echo "=== 验证3: 线程统计 ==="
jstack $PID | grep -c "java.lang.Thread.State"

# 验证4: 健康检查
echo ""
echo "=== 验证4: 健康检查 ==="
curl -s localhost:8080/actuator/health || echo "应用未响应"

echo ""
echo "=== 验证完成 ==="