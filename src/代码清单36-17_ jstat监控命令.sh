# 获取应用PID
PID=$(cat app.pid)
echo "监控PID: $PID"

# 每500ms输出一次GC统计，共10次
jstat -gcutil $PID 500 10

# 输出示例解读
# S0 S1 E O M YGC YGCT FGC FGCT GCT
# 0.00 0.00 35.42 67.89 98.25 123 4.567 2 0.345 4.912