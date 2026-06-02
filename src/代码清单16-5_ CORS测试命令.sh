# 测试简单请求（GET，无需预检）
curl -v http://localhost:8080/api/products/1 \
  -H "Origin: http://localhost:3000"

# 测试预检请求（OPTIONS）
curl -v -X OPTIONS http://localhost:8080/api/products \
  -H "Origin: http://localhost:3000" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: Authorization,Content-Type"

# 测试 POST 请求（带预检）
curl -v -X POST http://localhost:8080/api/products \
  -H "Origin: http://localhost:3000" \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Product"}'