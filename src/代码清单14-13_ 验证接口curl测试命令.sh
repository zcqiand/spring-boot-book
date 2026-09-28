# 测试1: 正常注册
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "Pass1234",
    "confirmPassword": "Pass1234",
    "email": "test@example.com",
    "phoneNumber": "13812345678"
  }'
# 期望: {"success": true, "message": "注册成功"}

# 测试2: 密码不一致
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "Pass1234",
    "confirmPassword": "Pass5678",
    "email": "test@example.com",
    "phoneNumber": "13812345678"
  }'
# 期望: {"success": false, "errors": {"confirmPassword": "两次密码输入不一致"}}

# 测试3: 手机号格式错误
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "Pass1234",
    "confirmPassword": "Pass1234",
    "email": "test@example.com",
    "phoneNumber": "12345"
  }'
# 期望: {"success": false, "errors": {"phoneNumber": "手机号格式不正确"}}

# 测试4: 缺少必填字段
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "",
    "password": "123",
    "confirmPassword": "123",
    "email": "invalid-email",
    "phoneNumber": "12345"
  }'
# 期望: 返回多个字段错误