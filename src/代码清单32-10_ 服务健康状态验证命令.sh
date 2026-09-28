# 测试MySQL连接
docker-compose exec mysql8 mysql -u root -p

# 测试Redis连接
docker-compose exec redis7 redis-cli ping

# 测试应用健康检查
curl http://localhost:8080/actuator/health

# 查看服务健康状态
docker-compose ps --format "table {{.Name}}\t{{.Status}}"