# 1. 启动MySQL、Redis、MongoDB服务

# 2. 创建数据库
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS demo_jpa"

# 3. 初始化数据库表
mysql -u root -p demo_jpa < src/main/resources/db/migration/V1__init_schema.sql

# 4. 启动应用
mvn spring-boot:run

# 5. 测试API
# 创建用户
curl -X POST http://localhost:8080/api/v1/data/users \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"$2a$10$xxx","email":"test@example.com"}'

# 查询用户
curl http://localhost:8080/api/v1/data/users/username/testuser

# 创建订单
curl -X POST http://localhost:8080/api/v1/data/orders \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"amount":99.99,"status":"PENDING"}'

# 添加评论
curl -X POST http://localhost:8080/api/v1/data/comments \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"username":"testuser","targetType":"PRODUCT","targetId":"PROD-001","content":"Great!","rating":5}'

# 获取用户统计
curl http://localhost:8080/api/v1/data/users/1/statistics