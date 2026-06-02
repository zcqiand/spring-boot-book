# 附加到进程（交互式）
jhsdb attach <pid>

# 堆直方图
jhsdb jmap --pid <pid> --histo

# 堆转储
jhsdb jmap --pid <pid> --dumpfile /tmp/heapdump.hprof

# 调试器（需要调试符号）
jhsdb hsdb

# 查看类加载器
jhsdb classloader <pid>

# 线程信息
jhsdb jstack --pid <pid>