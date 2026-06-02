# 查看内存映射（histogram格式）
jmap -histo <pid>

# 查看内存映射（前20项，按对象大小排序）
jmap -histo:file=/tmp/histo.log <pid>

# 查看存活对象（前20项）
jmap -histo:live <pid>

# 生成堆转储文件（hprof格式）
jmap -dump:format=b,file=/tmp/heapdump.hprof <pid>

# 生成带鲜活度统计的堆转储
jmap -dump:live,format=b,file=/tmp/live-heap.hprof <pid>

# 查看类加载器统计
jmap -clstats <pid>

# 查看对象统计
jmap -finalizerinfo <pid>