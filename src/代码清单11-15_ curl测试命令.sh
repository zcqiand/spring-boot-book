# 查询文章100的所有评论（分页）
curl -X GET "http://localhost:8080/api/v1/articles/100/comments?page=1&size=10"

# 发表评论（文章100）
curl -X POST "http://localhost:8080/api/v1/articles/100/comments" \
  -H "Content-Type: application/json" \
  -d '{"authorId": 1, "content": "这是一条评论"}'

# 修改评论（评论ID为5）
curl -X PUT "http://localhost:8080/api/v1/articles/100/comments/5" \
  -H "Content-Type: application/json" \
  -d '{"content": "修改后的评论内容"}'

# 删除评论
curl -X DELETE "http://localhost:8080/api/v1/articles/100/comments/5"