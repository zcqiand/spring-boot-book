# 构建镜像
docker build -t lab-system:latest .

# 运行容器
docker run -d \
  --name lab-system \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -v /data/lab-system/logs:/app/logs \
  lab-system:latest

# 查看运行状态
docker ps

# 查看日志
docker logs -f lab-system