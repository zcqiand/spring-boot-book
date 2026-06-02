PID=$(cat app.pid)

# 查看所有线程堆栈
jstack $PID

# 使用jhsdb查看更详细的线程信息
jhsdb jstack --pid $PID

# 统计线程状态
jstack $PID | grep "java.lang.Thread.State" | sort | uniq -c