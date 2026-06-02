# 触发GC
jcmd $PID GC.run

# 再次查看GC统计
jstat -gcutil $PID

# 查看GC原因
jstat -gccause $PID