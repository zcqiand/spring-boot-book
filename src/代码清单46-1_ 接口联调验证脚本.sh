#!/bin/bash
# 接口联调验证脚本

API_HOST="http://lab-system:8080"
TOKEN=$(curl -s -X POST "$API_HOST/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | jq -r '.data.token')

echo "=== 接口联调验证 ==="

# 1. 用户模块
echo "1. 用户模块接口验证..."
curl -s -X GET "$API_HOST/api/v1/users" \
  -H "Authorization: Bearer $TOKEN" | jq '.code' | grep -q 200 && echo "  ✅ 用户列表接口OK" || echo "  ❌ 用户列表接口失败"

# 2. 任务模块
echo "2. 任务模块接口验证..."
TASK_RESP=$(curl -s -X POST "$API_HOST/api/v1/tasks" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"联调测试任务","labId":1,"creatorId":1,"priority":"NORMAL"}')
TASK_ID=$(echo $TASK_RESP | jq -r '.data.taskId')
echo "  创建任务返回: $TASK_RESP"

# 3. 设备模块
echo "3. 设备模块接口验证..."
curl -s -X GET "$API_HOST/api/v1/equipment" \
  -H "Authorization: Bearer $TOKEN" | jq '.code' | grep -q 200 && echo "  ✅ 设备列表接口OK" || echo "  ❌ 设备列表接口失败"

# 4. 报告模块
echo "4. 报告模块接口验证..."
curl -s -X GET "$API_HOST/api/v1/reports" \
  -H "Authorization: Bearer $TOKEN" | jq '.code' | grep -q 200 && echo "  ✅ 报告列表接口OK" || echo "  ❌ 报告列表接口失败"

echo "=== 联调验证完成 ==="