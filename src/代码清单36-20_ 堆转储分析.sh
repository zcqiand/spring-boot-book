PID=$(cat app.pid)

# 生成堆转储
jcmd $PID GC.heap_dump filename=/tmp/heap_analysis.hprof

# 查看对象直方图
jcmd $PID GC.heap_dump filename=/tmp/histo.hprof format=hprof

# 在应用运行过程中触发内存分配
curl -X GET "http://localhost:8080/api/memory/allocate/50"
curl -X GET "http://localhost:8080/api/memory/allocate/50"

# 再次生成堆转储对比
jcmd $PID GC.heap_dump filename=/tmp/heap_after.hprof