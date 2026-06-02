# 打印GC日志
-verbose:gc -Xlog:gc*:file=/var/log/app-gc.log:time,uptime:filecount=5,filesize=10M

# 远程调试
-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005