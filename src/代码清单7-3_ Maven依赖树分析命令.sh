# 查看完整依赖树
mvn dependency:tree

# 过滤只看特定依赖
mvn dependency:tree -Dincludes=*:spring-*

# 导出到文件方便分析
mvn dependency:tree -DoutputFile=deps.txt