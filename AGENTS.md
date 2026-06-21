# 项目说明（AI 助手指南）

本文件为仓库文档主体，供 Claude Code 与 Codex 等 AI 助手了解本仓库。

> 本仓库采用「AGENTS.md 主体 + CLAUDE.md 导入」方案：Codex 读 `AGENTS.md`，Claude Code 读 `CLAUDE.md`（经 `@AGENTS.md` 导入本文件），二者内容一致、无需重复维护。
> 各子模块根目录也提供独立的 `CLAUDE.md`/`AGENTS.md`，描述该模块的细节。

## 项目概述

SpringBoot / SpringCloud 组件实践 Starter 项目，提供常用基础配置、增强与代码生成工具，供业务项目直接依赖。

- **JDK 17**（`maven.compiler.source/target=17`）
- **Spring Boot 2.7.18**、**Spring Cloud 2021.0.9**、**Spring Cloud Alibaba 2021.0.6.0**
- **MyBatis-Plus 3.5.7**、**Redisson 3.17.7**、**Knife4j 4.5.0**、**MapStruct 1.6.3**、**Lombok 1.18.38**、**Flyway 9.22.3**
- Maven 多模块聚合构建；版本统一为 `4.1.0-SNAPSHOT`

## 版本与构建机制（重要）

- 所有模块版本使用 CI-Friendly 占位符 `${revision}`，真实值在根 `pom.xml` 的 `<properties><revision>` 定义。
- 通过 `flatten-maven-plugin`（`resolveCiFriendliesOnly`）在 `process-resources` 阶段生成扁平化 POM，因此根目录会出现 `.flattened-pom.xml`。
- **升级版本号只改根 `pom.xml` 与 `wei-dependencies/pom.xml` 的 `revision`**，不要在子模块写死版本。
- 子模块间互相依赖时统一写 `<version>${revision}</version>`。

## 常用命令

```bash
# 全量编译并安装到本地仓库（首次必须，子模块互相依赖）
mvn clean install

# 跳过测试
mvn clean install -DskipTests

# 仅构建某个模块（-am 连带构建其依赖的模块）
mvn clean install -pl wei-starter-mybatis -am

# 运行某示例模块的全部测试
mvn test -pl springboot-starter-example/springboot-starter-example-mybatis

# 运行单个测试类
mvn test -pl springboot-starter-example/springboot-starter-example-mybatis -Dtest=UserAuthPhoneServiceTest

# 运行单个测试方法
mvn test -pl springboot-starter-example/springboot-starter-example-mybatis -Dtest=UserAuthPhoneServiceTest#方法名

# 发布到 Maven 中央仓库
mvn clean deploy -P deploy-center
```

> 注意：修改任一 `wei-starter-*` 后，依赖它的示例/其它模块需要先 `mvn install` 才能取到最新改动。

## 模块全景

聚合关系：`springcloud-starter` → `springboot-starter` → (`base` + `mybatis` + `redis` + `swagger`)。

| 模块 | 类型 | 职责 |
|------|------|------|
| `wei-dependencies` | BOM | 统一依赖版本管理，被所有模块 `import` |
| `springboot-starter` | 聚合 starter | 一次性引入 base/mybatis/redis/swagger |
| `springcloud-starter` | 聚合 starter | 在 springboot-starter 之上叠加 Nacos(配置/注册)、SkyWalking、bootstrap |
| `wei-starter-base` | 功能 starter | 通用 Bean、异常、校验分组、工具类、MVC/JSON 配置、日志切面、**Token 安全** |
| `wei-starter-mybatis` | 功能 starter | MyBatis-Plus 配置、基础实体、BaseService、XMapper、SQL 耗时/参数插件 |
| `wei-starter-redis` | 功能 starter | Redis 多 Key 原子操作 + 声明式缓存(`@RedisCacheable`) + 声明式分布式锁(`@Lock`) |
| `wei-starter-sequence` | 功能 starter | 基于数据库双缓冲的序号生成 |
| `wei-starter-swagger` | 功能 starter | Knife4j/Springfox 文档与 UI |
| `springboot-starter-code-plugin` | Maven 插件 | MyBatis Generator + Freemarker 代码生成 |
| `springboot-starter-archetype` | Maven 原型 | 生成 api/service/web 三层多模块业务骨架 |
| `springboot-starter-example` | 示例 | 各 starter 的可运行示例 |

> 历史变更：旧文档中的 `wei-starter-cache`、`wei-starter-lock` 已合并进 `wei-starter-redis`；`wei-starter-security` 已合并进 `wei-starter-base`。

## 自动配置约定

各 starter 仍使用 **Spring Boot 2.x 的 `META-INF/spring.factories`**（非 3.x 的 `.imports`）注册 `EnableAutoConfiguration`。新增自动配置类时，必须在对应模块的 `spring.factories` 中登记，否则不会生效。

## 包与命名约定

- 核心模块包名：`com.wei.starter.*`
- 根 groupId：`com.github.weijj0528`；示例项目包名：`com.github.weijj0528.example.*`
- 工具类统一 `Wei` 前缀：`WeiBeanUtil`、`WeiWebUtils`、`WeiJsonUtils`、`WeiObjUtils`、`WeiComUtils`、`WeiRedisUtils`、`WeiSecurityUtil`
- 异常体系：继承 `BaseException`，含 `BadRequestException`、`UnauthorizedException`、`ForbiddenException`、`OvertimeException`、`ErrorMsgException`

## 通用 Bean（`wei-starter-base`）

- `Result<T>`：统一响应（`code`/`msg`/`time`/`data`）；成功码 `20000`，用 `Code.SUCCESS` 判定，`result.successfully()` 判成功
- `Page<T>`：分页（`list`/`page`/`size`/`total`）
- `Header`：请求头封装（token、version、sign、devices 等）
- `Range<T>`：范围参数（`from`/`to`）
- `Code`：错误码枚举

## 校验分组

Bean Validation 分组接口位于 `com.wei.starter.base.valid`：
`Add`、`Save`、`Del`、`Remove`、`Update`、`Edit`、`Query`、`Select`、`Close`、`Open`、`Apply`、`Audit`、`Init`、`Lock`、`Unlock`、`Sub`、`Setting`。

## 数据库版本控制

使用 Flyway，迁移脚本放在业务项目的 `src/main/resources/db/migration/`，命名 `V{版本}__{描述}.sql`。

## 示例项目

`springboot-starter-example/` 下每个子模块都有独立可运行的 `*Application`：
`BaseExampleApplication`、`MybatisExampleApplication`、`RedisExampleApplication`、`SequenceExampleApplication`、`Swagger2ExampleApplication`。
