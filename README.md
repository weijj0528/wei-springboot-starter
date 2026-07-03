# wei-springboot-starter

SpringBoot / SpringCloud 组件实践 Starter，提供常用基础配置、增强与代码生成工具，供业务项目直接依赖。

## 特性

- **统一响应封装**：`Result<T>` / `Page<T>` / `Code`，全局异常处理自动转 HTTP 状态码
- **声明式分布式锁**：`@Lock` / `@Locking` 注解，Redis 与 Redisson 双实现
- **声明式缓存**：`@RedisCacheable` + `@CacheTtl` 动态过期，Spring Cache 增强
- **Token 安全**：`WeiTokenFilter` + `TokenService` 抽象，默认 HMAC 签名实现（fail-closed）
- **MyBatis-Plus 增强**：`AbstractService` / `BaseService` / `XMapper` / 自动填充 / SQL 耗时插件
- **序号生成**：基于数据库双缓冲的分布式序号生成
- **API 文档**：Knife4j 集成与一套美观 UI
- **代码生成**：MyBatis Generator Maven 插件 + 业务三层骨架 Maven 原型

## 技术栈

| 项 | 版本 |
|----|------|
| JDK | 17 |
| Spring Boot | 2.7.18 |
| Spring Cloud | 2021.0.9 |
| Spring Cloud Alibaba | 2021.0.6.0 |
| MyBatis-Plus | 3.5.7 |
| Redisson | 3.17.7 |
| Knife4j | 4.5.0 |
| MapStruct | 1.6.3 |
| Lombok | 1.18.38 |
| Flyway | 9.22.3 |

## 模块全景

| 模块 | 类型 | 职责 |
|------|------|------|
| `wei-dependencies` | BOM | 统一依赖版本管理，被所有模块 `import` |
| `springboot-starter` | 聚合 starter | 一次性引入 base / mybatis / redis / swagger |
| `springcloud-starter` | 聚合 starter | 在 springboot-starter 之上叠加 Nacos（配置/注册）、SkyWalking、bootstrap |
| `wei-starter-base` | 功能 starter | 通用 Bean、异常体系、校验分组、工具类、MVC/JSON 配置、日志切面、**Token 安全** |
| `wei-starter-mybatis` | 功能 starter | MyBatis-Plus 配置、基础实体、BaseService、XMapper、SQL 耗时/参数插件 |
| `wei-starter-redis` | 功能 starter | Redis 多 Key 原子操作 + 声明式缓存（`@RedisCacheable`）+ 声明式分布式锁（`@Lock`） |
| `wei-starter-sequence` | 功能 starter | 基于数据库双缓冲的序号生成 |
| `wei-starter-swagger` | 功能 starter | Knife4j / Springfox 文档与 UI |
| `springboot-starter-code-plugin` | Maven 插件 | MyBatis Generator + Freemarker 代码生成 |
| `springboot-starter-archetype` | Maven 原型 | 生成 api / service / web 三层多模块业务骨架 |
| `springboot-starter-example` | 示例 | 各 starter 的可运行示例 |

> 历史变更：`wei-starter-cache`、`wei-starter-lock` 已合并进 `wei-starter-redis`；`wei-starter-security` 已合并进 `wei-starter-base`。

## 快速开始

### 引入依赖

业务项目只需引入聚合 starter，版本由 `wei-dependencies` BOM 统一管理：

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.github.weijj0528</groupId>
            <artifactId>wei-dependencies</artifactId>
            <version>4.1.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <!-- 单体应用：引入 base / mybatis / redis / swagger -->
    <dependency>
        <groupId>com.github.weijj0528</groupId>
        <artifactId>springboot-starter</artifactId>
        <version>4.1.0</version>
    </dependency>

    <!-- 微服务应用：在 springboot-starter 之上叠加 Nacos / SkyWalking -->
    <dependency>
        <groupId>com.github.weijj0528</groupId>
        <artifactId>springcloud-starter</artifactId>
        <version>4.1.0</version>
    </dependency>
</dependencies>
```

也可按需引入单个功能 starter：`wei-starter-base`、`wei-starter-mybatis`、`wei-starter-redis`、`wei-starter-sequence`、`wei-starter-swagger`。

### 生成业务骨架

使用 Maven 原型快速生成 api / service / web 三层多模块项目：

```bash
mvn archetype:generate \
  -DarchetypeGroupId=com.github.weijj0528 \
  -DarchetypeArtifactId=springboot-starter-archetype \
  -DarchetypeVersion=4.1.0 \
  -DgroupId=com.example \
  -DartifactId=demo \
  -Dversion=1.0.0
```

## 核心能力

### 统一响应

```java
@GetMapping("/hello")
public Result<QueryDto> hello(QueryDto query) {
    return Result.success(query);  // code=20000
}
// 失败：Result.fail(Code.SYSTEM_ERROR)
```

`Result<T>` 字段：`code` / `msg` / `time` / `data`；成功码 `20000`，用 `Code.SUCCESS` 判定，`result.successfully()` 判成功。

### 全局异常处理

继承 `BaseException` 的异常自动转统一响应：

| 异常 | HTTP 状态 |
|------|-----------|
| `UnauthorizedException` | 401 |
| `ForbiddenException` / `AccessDeniedException` | 403 |
| `ErrorMsgException` / 系统异常 | 500 |
| `BadRequestException` | 400 |
| `OvertimeException` | 408 |

### 声明式分布式锁

```java
@Locking(@Lock(lockName = "order", key = "#orderId", waitTime = 3, expiredTime = 10))
public void process(String orderId) { ... }
```

- `RedisLock`：基于 `SET NX PX` + UUID，**不可重入**
- `RedissonLock`：基于 Redisson `RLock`，**可重入**
- classpath 有 `redisson-spring-boot-starter` 时自动用 Redisson 实现

### 声明式缓存

```java
@RedisCacheable("user_cache")
@CacheTtl(600)  // 动态 TTL，单位秒；类上声明则全方法继承
public User getUser(Long id) { ... }
```

`@RedisCacheable` 指定缓存管理器与键生成器；`@CacheTtl` 支持方法级与类级动态过期。

### Token 安全

```yaml
spring:
  security:
    custom:
      enable: true                       # 开启安全
      token-secret: ${SECRET_KEY}        # SimpleTokenService HMAC 签名密钥
      open-apis:                         # 放行接口
        - GET:/example/hello
        - /security/login
```

实现 `TokenService` 接口替换默认 `SimpleTokenService`（HMAC-SHA256 无状态签名，未配置密钥时 fail-closed）。

### MyBatis-Plus 增强

```java
@Service
public class UserService extends AbstractService<User> implements UserService {
    @Override
    public XMapper<User> getMapper() { return userMapper; }
    // 自动获得 insertSelective / insertList / selectPageByExample / cursorOperator 等
}
```

`Entity` 基类含 `id` / `tenant` / `version` / `deleted` / `creator` / `updater` / `ctime` / `utime`，`WeiMetaObjectHandler` 自动填充审计字段。

## 构建与测试

```bash
# 全量编译并安装到本地仓库（首次必须，子模块互相依赖）
mvn clean install

# 跳过测试
mvn clean install -DskipTests

# 仅构建某个模块（-am 连带构建其依赖）
mvn clean install -pl wei-starter-mybatis -am

# 运行测试
mvn test

# Redis 集成测试（需 Docker，基于 Testcontainers 自动起容器）
mvn test -pl springboot-starter-example/springboot-starter-example-redis
```

> 修改任一 `wei-starter-*` 后，依赖它的示例/其它模块需要先 `mvn install` 才能取到最新改动。

## 版本与构建机制

- 所有模块版本使用 CI-Friendly 占位符 `${revision}`，真实值在根 `pom.xml` 与 `wei-dependencies/pom.xml` 的 `<revision>` 定义
- `flatten-maven-plugin` 在 `process-resources` 阶段生成扁平化 POM
- **升级版本号只改根 `pom.xml` 与 `wei-dependencies/pom.xml` 的 `revision`**，不要在子模块写死版本
- `maven-compiler-plugin 3.13.0` + `<release>17</release>`

## 发布到 Maven 中央仓库

发布配置已就绪（`deploy-center` profile，迁移至 Sonatype Central Portal）：

```bash
# 配置好 GPG 密钥与 Central Portal 账号后
mvn clean deploy -P deploy-center -DskipTests
```

详细发布流程（GPG、settings.xml、命名空间验证）见发布手册。

## 自动配置约定

各 starter 使用 Spring Boot 2.x 的 `META-INF/spring.factories` 注册 `EnableAutoConfiguration`。新增自动配置类时必须在对应模块的 `spring.factories` 中登记。

## 包与命名约定

- 核心模块包名：`com.wei.starter.*`
- 根 groupId：`com.github.weijj0528`
- 示例项目包名：`com.github.weijj0528.example.*`
- 工具类统一 `Wei` 前缀：`WeiBeanUtil`、`WeiWebUtils`、`WeiJsonUtils`、`WeiRedisUtils`、`WeiSecurityUtil` 等
- 异常体系：继承 `BaseException`

## 数据库版本控制

使用 Flyway，迁移脚本放在业务项目的 `src/main/resources/db/migration/`，命名 `V{版本}__{描述}.sql`。

## License

[Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)
