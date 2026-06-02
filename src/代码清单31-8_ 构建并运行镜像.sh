# 构建镜像
docker build -t docker-demo:1.0.0 .

# 运行容器
docker run -d \
  --name docker-demo \
  -p 8080:8080 \
  --memory=512m \
  -e JAVA_OPTS="-Xms128m -Xmx256m" \
  docker-demo:1.0.0

# 验证运行状态
docker ps

# 测试接口
curl http://localhost:8080/actuator/health