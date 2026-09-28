# 查询所有可借阅图书
curl -X GET "http://localhost:8080/api/v1/books?available=true" \
  -H "Accept: application/json"

# 创建借阅记录
curl -X POST "http://localhost:8080/api/v1/borrows" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d '{"userId": 1, "bookId": 101}'

# 查询用户借阅历史（嵌套资源）
curl -X GET "http://localhost:8080/api/v1/users/1/borrows" \
  -H "Accept: application/json"

# 归还图书（PUT作为子资源动作，可接受）
curl -X PUT "http://localhost:8080/api/v1/borrows/1/return" \
  -H "Content-Type: application/json"

# 查询特定借阅记录
curl -X GET "http://localhost:8080/api/v1/borrows/1" \
  -H "Accept: application/json"