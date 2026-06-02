# 开发环境启动
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# CI/CD集成
mvn flyway:migrate -Dflyway.url=jdbc:mysql://prod-db:3306/app_db