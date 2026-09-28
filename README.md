# **Spring Boot 从入门到项目实践**- 代码清单

> **本书配套代码示例库** — 从章节中提取的完整可运行代码

## 关于本书

Spring Boot 已成为 Java 企业级开发的事实标准。然而，市面上大多数 Spring Boot 书籍基于 2.x 版本编写，内容滞后于最新版本；对于企业级应用的实战指导更是凤毛麟角。本书致力于填补这一空白——基于 Spring Boot 3.x，提供从核心概念到企业级实战的全链路覆盖。

本书的写作目标是让读者不仅"会用"Spring Boot，更能理解"什么时候用""为什么这样用"，并在阅读完成后具备交付生产级 Spring Boot 应用的能力。

## 本书特点

**第一，应用目标驱动。** 每一章开头都明确说明「学完这章你能交付什么」，并且这些目标都是真实的可交付成果。

**第二，两个完整生产级项目。** 建筑工程实验室管理系统和 SaaS 统一身份管理系统两个企业级案例贯穿始终。

**第三，扩展机制的实战组合。** 对比表格和决策框架帮你建立选择直觉。

## 谁应该读这本书

如果你是一名有一年及以上编程经验的开发者，熟悉 Java 和 Spring MVC，希望将 Spring Boot 能力融入日常工作，这本书适合你。你可能是：

- 在互联网公司工作的后端工程师，希望用 Spring Boot 提升开发效率
- 独立开发者或小团队成员，需要一个人完成从需求到部署的全流程
- 对 Spring Boot 3.x 新特性感兴趣的技术爱好者

## 代码清单说明

本目录包含从书籍章节中提取的 615 个代码示例文件，涵盖 56 个章节的核心知识点。

### 📊 代码统计

- **总文件数**: 615 个
- **涉及章节**: 第 1-56 章

### 📋 按编程语言分类

| 语言       | 文件数 | 扩展名          |
| ---------- | ------ | --------------- |
| java       | 384    | `.java`       |
| bash       | 76     | `.sh`         |
| yaml       | 63     | `.yml`        |
| xml        | 23     | `.xml`        |
| sql        | 23     | `.sql`        |
| properties | 17     | `.properties` |
| dockerfile | 8      | `.dockerfile` |
| python     | 6      | `.py`         |
| json       | 3      | `.json`       |
| html       | 3      | `.html`       |
| plaintext  | 3      | `.txt`        |
| groovy     | 2      | `.groovy`     |
| javascript | 2      | `.js`         |
| text       | 1      | `.txt`        |
| markdown   | 1      | `.md`         |

## 如何使用代码

### 环境准备

- **Java**: 17+
- **Maven**: 3.9+
- **Spring Boot**: 3.4.x

### 文件命名规范

```text
chapter{章节号:02d}_{描述/类名/函数名}.{扩展名}
```

示例：

- `chapter05_code12.java` - 第5章的Java示例
- `chapter10_code7.yml` - 第10章的YAML配置

### 运行示例

**Java 代码**:

```bash
javac src/chapter05_code12.java
java -cp src Chapter05Code12
```

**YAML 配置**: 直接放到 Spring Boot 项目的 `src/main/resources/` 目录

### 代码说明

每个代码文件开头都包含来源注释：

```java
// 从第 5 章提取
// 来源：Spring Boot 3.x 企业级实战
```

## 配套资源

- **GitHub 仓库**: [https://github.com/zcqiand/spring-boot-book](https://github.com/zcqiand/spring-boot-book)
- **勘误页面**: [https://github.com/zcqiand/spring-boot-book/issues](https://github.com/zcqiand/spring-boot-book/issues)
- **读者交流**: 1282301776@qq.com

## ⚠️ 注意事项

1. **代码版本**: 代码基于 Spring Boot 3.4.x / Java 21 / Maven 3.9+
2. **依赖安装**: 运行前请确保已安装对应语言的开发环境
3. **安全审查**: 生产环境使用前请审查代码，特别是涉及认证和权限的部分
4. **环境差异**: 部分代码可能需要根据实际环境调整

---

**最后更新**: 2026年6月2日
**书籍版本**: 1.0
**代码来源**: [../chapters](../chapters/)
