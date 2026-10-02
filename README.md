# Spring Boot 从入门到项目实践 - 代码清单

## 关于本书

《Spring Boot 从入门到项目实践》是一本基于 Spring Boot 3.4.x 与 Java 21 的企业级开发实战书，从自动配置等核心机制一路讲到生产级多租户架构，目标是让读者不仅「会用」Spring Boot，更理解「什么时候用」「为什么这样用」，并具备交付生产级应用的能力。

全书 56 章，两个完整生产级项目贯穿始终：建筑工程实验室管理系统（Spring Boot 3.4.1 + PostgreSQL 18）与 SaaS 多租户统一身份管理平台（Spring Boot 3.4.0 + PostgreSQL 18），案例章代码全部绑定真实可跑工程；路线从项目初始化、数据访问、事务与测试，延伸到 OAuth 令牌签发、多租户数据隔离，直至 Kubernetes 部署，学完能独立交付完整的 SaaS 身份管理系统。如果你是一名有一年及以上编程经验、熟悉 Java 与 Spring MVC 的开发者，这本书适合你：希望用 Spring Boot 提升开发效率的后端工程师、需要一个人走完从需求到部署全流程的独立开发者或小团队成员，以及对 Spring Boot 3.x 新特性感兴趣的技术爱好者。

## 本书特点

**第一，应用目标驱动。** 每一章开头都明确「学完这章你能交付什么」，并且这些目标都是真实的可交付成果。

**第二，两个完整生产级项目。** 建筑工程实验室管理系统与 SaaS 统一身份管理系统两个企业级案例贯穿始终，案例工程已冻结 tag，clone 即跑。

**第三，扩展机制的实战组合。** 用对比表格和决策框架帮你建立「什么时候用、为什么这样用」的选择直觉，而非死记最佳实践。

**第四，生产级终点线。** 从自动配置一路推进到多租户架构与 Kubernetes 部署，读完即具备独立交付生产级 Spring Boot 应用的能力。

## 案例仓库

| 仓库名 | 说明 |
| :--- | :--- |
| [lab-management-system-springboot](https://github.com/zcqiand/lab-management-system-springboot) @ v0.1.47-20260926 | 基于 Java 21 / Spring Boot 3.4.1 / PostgreSQL 18 的建筑工程实验室管理系统实战项目，覆盖检测样本、检测项目目录与报表流程等业务模块 |
| [saas-identity-platform-springboot](https://github.com/zcqiand/saas-identity-platform-springboot) @ v0.2.34-20260926 | 基于 Java 21 / Spring Boot 3.4.0 / PostgreSQL 18 的 SaaS 多租户统一身份管理平台实战项目，覆盖多租户、JWT 认证、OAuth 令牌与审计拦截 |

> 配套案例仓库为独立可跑工程，已冻结 tag，含完整测试与 CI，clone 即跑。

## 代码清单说明

本书所有代码清单均收录于本目录，对应书稿中「代码清单 N-M」标题块。

### 运行环境

```bash
# 环境要求：JDK 21 + Maven 3.9+（案例仓库为 Spring Boot 3.4.x 工程），另需 PostgreSQL 18 数据库
java -version
mvn -version
docker run --name pg18-book -e POSTGRES_PASSWORD=pg123456 -p 5432:5432 -d postgres:18
# 克隆案例仓库、切到冻结 tag 后编译并启动（以实验室管理系统为例）
git clone https://github.com/zcqiand/lab-management-system-springboot.git
cd lab-management-system-springboot && git checkout v0.1.47-20260926
# 数据库连接按仓库内 resources 配置文件调整为实际环境
mvn clean package
mvn spring-boot:run
# 运行单个清单文件：Java 清单为案例仓实码摘录，放回 src/main/java 对应包路径后随工程编译运行
mvn compile
# 运行工程自带测试
mvn test
```

### 目录结构

```
src/
├── 代码清单1-* … 代码清单56-*   # 第 1-56 章，共 561 个清单文件（命名「代码清单N-M_ 描述.扩展名」）
└── extracted_code_manifest.json   # 全部清单索引（title/lang/chapter_file/line/extracted_file/source）
```
