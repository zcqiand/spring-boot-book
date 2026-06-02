# 查看类加载统计（每250ms采样，共5次）
jstat -class <pid>

# 查看编译统计
jstat -compiler <pid>

# 查看GC统计（每1秒采样，持续输出）
jstat -gc <pid> 1000

# 查看GC容量统计
jstat -gccapacity <pid>

# 查看GC利用率统计
jstat -gcutil <pid> 1000

# 特定GC区域查看（S0/S1/Eden/Old/Metaspace）
jstat -gccause <pid>

# 输出示例：每列含义 S0 S1 E O M CC S YGC YGCT FGC FGCT CGC CGCT
# S0=Survivor0利用率 S1=Survivor1利用率 E=Eden区利用率
# O=老年代利用率 M=元空间利用率 YGC=年轻代GC次数 YGCT=年轻代GC时间
# FGC=Full GC次数 FGCT=Full GC时间