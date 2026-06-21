# CLAUDE.md — wei-starter-base

> 本文件为模块文档主体；Codex 直接读取，Claude Code 经同目录 `CLAUDE.md` 的 `@` 导入读取。顶层架构见仓库根 `AGENTS.md`。

## 职责

基础 starter，被所有其它模块隐式依赖。提供：通用 Bean、异常体系、校验分组、`Wei*` 工具类、MVC/JSON 全局配置、日志切面、全局异常处理，以及**基于 Token 的安全认证**。

## 包结构（`com.wei.starter`）

- `base/bean` — `Result<T>`、`Page<T>`、`Header`、`Range<T>`、`Code`
- `base/exception` — `BaseException` 及其子类（BadRequest/Unauthorized/Forbidden/Overtime/ErrorMsg）
- `base/valid` — 校验分组接口（Add/Save/Update/Query/Audit…）
- `base/util` — `WeiBeanUtil`、`WeiWebUtils`、`WeiJsonUtils`、`WeiObjUtils`、`WeiComUtils`
- `base/config` — `EnvConfig`、`SpringContext`、`JsonAutoConfig`、`WeiMvcConfigurer`、日期序列化(`DateSerializer`/`DateDeserializer`)
- `base/advice` — `ApiLogAspect`（接口日志 + MDC 链路追踪）、`GlobalExceptionHandler`（统一异常→`Result`）
- `base/wrapper` — `MDCRunnableWrapper`（跨线程传递 MDC）
- `security` — Token 安全：`TokenService`/`SimpleTokenService`、`WeiToken`、`Principal`、`WeiRole`、`WeiSecurityConfig`、`WeiTokenFilter`、`WeiSecurityUtil`

## 自动配置

`META-INF/spring.factories` 注册：`EnvConfig`、`SpringContext`、`JsonAutoConfig`、`WeiMvcConfigurer`、`ApiLogAspect`、`GlobalExceptionHandler`、`WeiSecurityProperties`、`WeiSecurityConfig`。新增配置类务必在此登记。

## 关键约定

- `Result` 成功码为 `20000`，判定走 `Code.SUCCESS` / `result.successfully()`。
- 安全配置属性前缀 `spring.security.custom`（`WeiSecurityProperties`）。
- 自定义 Token 存储/鉴权时实现 `TokenService` 接口；`permissionCheck(principal, httpMethod, pattern)` 为权限校验入口。
- `SpringContext` 提供静态获取 Bean 的能力，谨慎使用。
