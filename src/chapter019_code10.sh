   # 查询所有
   curl http://localhost:8080/api/users
   
   # 分页查询
   curl "http://localhost:8080/api/users/page?page=0&size=10"
   
   # 根据ID查询
   curl http://localhost:8080/api/users/1