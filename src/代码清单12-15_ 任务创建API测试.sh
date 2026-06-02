# 创建任务（最小参数）
curl -X POST "http://localhost:8080/api/projects/100/tasks" \
  -H "Content-Type: application/json" \
  -H "X-Operator-Id: user123" \
  -d '{"title": "完成第一章", "description": "编写Spring Boot入门"}'

# 创建任务（带可选Query参数）
curl -X POST "http://localhost:8080/api/projects/100/tasks?priority=high" \
  -H "Content-Type: application/json" \
  -H "X-Operator-Id: user456" \
  -H "Cookie: lang=zh-CN" \
  -d '{"title": "完成第二章", "description": "编写Spring MVC"}'

# 查询任务（带Query参数）
curl -X GET "http://localhost:8080/api/projects/100/tasks?priority=high&dueDate=2024-12-31"