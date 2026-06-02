# 测试业务异常（409）
curl -X GET http://localhost:8080/api/users/-1

# 测试资源不存在（404）
curl -X GET http://localhost:8080/api/users/0