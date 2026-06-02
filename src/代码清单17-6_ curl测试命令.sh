# 测试公开接口（应成功）
curl -X GET http://localhost:8080/api/public/health

# 测试受保护接口（无Token，应返回401）
curl -X GET http://localhost:8080/api/users/1

# 测试受保护接口（有Token，应成功或根据权限返回结果）
curl -X GET http://localhost:8080/api/users/1 \
  -H "Authorization: Bearer test-token-12345" \
  -H "X-User-Id: user-001" \
  -H "X-User-Role: admin"