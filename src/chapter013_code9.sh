# 创建文章
curl -i -X POST http://localhost:8080/api/articles \
  -H "Content-Type: application/json" \
  -d '{"title":"Spring Boot实战","content":"深入学习Spring Boot","author":"alice"}'

# 获取文章（默认JSON）
curl http://localhost:8080/api/articles/1

# 获取文章（XML格式）
curl -H "Accept: application/xml" http://localhost:8080/api/articles/1

# 检查缓存头
curl -I http://localhost:8080/api/articles/1

# 测试ETag（二次请求应返回304）
curl -I -H "If-None-Match:\"1\"" http://localhost:8080/api/articles/1

# 测试404
curl http://localhost:8080/api/articles/999