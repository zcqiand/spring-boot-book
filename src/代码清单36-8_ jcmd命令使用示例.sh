# 查看可用命令列表
jcmd <pid> help

# 查看JFR相关命令
jcmd <pid> JFR.

# 启动JFR录制
jcmd <pid> JFR.start name= profiling delay=60s duration=300s filename=/tmp/profiling.jfr

# 检查JFR录制状态
jcmd <pid> JFR.check

# 停止JFR录制
jcmd <pid> JFR.stop name=profiling

# 转储JFR录制
jcmd <pid> JFR.dump name=profiling filename=/tmp/dump.jfr

# 生成GC堆转储
jcmd <pid> GC.heap_dump filename=/tmp/heapdump.hprof

# 执行GC
jcmd <pid> GC.run

# 查看VM标志
jcmd <pid> VM.flags

# 查看系统属性
jcmd <pid> VM.system_properties