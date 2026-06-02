   curl -X PUT http://localhost:8080/api/users/1 \
     -H "Content-Type: application/json" \
     -d '{"username":"alice2","email":"alice2@example.com"}'