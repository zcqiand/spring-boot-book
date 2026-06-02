#!/bin/bash
# JFR性能剖析脚本

PID=$1
DURATION=${2:-60}
OUTPUT_DIR="/tmp/jfr-analysis"

mkdir -p $OUTPUT_DIR

echo "=== 开始JFR录制 ==="
# 启动录制
jcmd $PID JFR.start name=perf-analysis \
  settings=profile \
  delay=10s \
  duration=${DURATION}s \
  filename=$OUTPUT_DIR/perf-analysis.jfr

echo "=== 等待录制完成 ==="
sleep $((DURATION + 15))

echo "=== 检查录制状态 ==="
jcmd $PID JFR.check

echo "=== 录制文件保存至 ==="
echo "$OUTPUT_DIR/perf-analysis.jfr"

# 使用jfr工具分析
echo "=== 生成报告 ==="
jfr print --json $OUTPUT_DIR/perf-analysis.jfr > $OUTPUT_DIR/report.json

# 提取关键信息
echo "=== GC事件 ==="
jfr print --events GC $OUTPUT_DIR/perf-analysis.jfr

echo "=== CPU负载 ==="
jfr print --events CPULoad $OUTPUT_DIR/perf-analysis.jfr

echo "=== 锁竞争 ==="
jfr print --events JVMTI.jvmti_monitor_wait $OUTPUT_DIR/perf-analysis.jfr