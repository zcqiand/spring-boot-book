# 启动应用并查看日志
./mvnw spring-boot:run

# 查看日志文件内容
cat logs/app.log

# 实时跟踪日志（Linux/Mac）
tail -f logs/app.log

# 实时跟踪日志（Windows PowerShell）
Get-Content logs/app.log -Wait -Tail 20