# 构建镜像，指定标签
docker build -t myapp:1.0.0 .

# 为镜像添加多个标签
docker tag myapp:1.0.0 registry.example.com/myapp:1.0.0
docker tag myapp:1.0.0 registry.example.com/myapp:latest

# 登录仓库
docker login registry.example.com

# 推送镜像
docker push registry.example.com/myapp:1.0.0
docker push registry.example.com/myapp:latest