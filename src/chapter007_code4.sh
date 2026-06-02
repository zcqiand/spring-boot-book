# 使用verbose模式查看被拒绝的依赖
mvn dependency:tree -Dverbose | grep "omitted for conflict"