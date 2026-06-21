# CLAUDE.md — wei-starter-mybatis

> 本文件为模块文档主体；Codex 直接读取，Claude Code 经同目录 `CLAUDE.md` 的 `@` 导入读取。顶层架构见仓库根 `AGENTS.md`。

## 职责

集成 **MyBatis-Plus 3.5.7**，提供基础实体、增强的 BaseService、Mapper 基接口与 SQL 增强插件。

## 包结构（`com.wei.starter.mybatis`）

- `config/MybatisPlusConfig` — 自动配置（分页插件、乐观锁等），在 `spring.factories` 注册
- `entity/Entity<T>` — 基础实体，内置字段：`id`(自增)、`tenant`(租户)、`version`(`@Version` 乐观锁)、`deleted`(`@TableLogic` 逻辑删除 0/1)、`creator/ctime/updater/utime`(审计字段)
- `plus/WeiMetaObjectHandler` — 自动填充 version/deleted/审计字段的处理器
- `service/BaseService<T>` + `AbstractService` — 继承 MP 的 `IService`，额外提供 `insertSelective`、`insertList`、`*ByPrimaryKey*` 等方法
- `xmapper/XMapper<T>` — 继承 MP `BaseMapper`；**注意：此接口本身不可被扫描**，仅供业务 Mapper 继承
- `plugin/` — `SqlCostInterceptor`(SQL 耗时打印)、`ArgsInterceptor`/`ArgsProvider`(通用参数注入)

## 关键约定

- 业务实体继承 `Entity<T>`，复用逻辑删除/乐观锁/审计字段；插入/更新时由 `WeiMetaObjectHandler` 自动填充，无需手动赋值。
- 业务 Service 继承 `AbstractService` 并实现 `BaseService<T>`；Mapper 继承 `XMapper<T>`。
- 字段名常量定义在 `Entity`（`ID`/`TENANT`/`VERSION`/`DELETED`/`UPDATER`/`UTIME`/`CREATOR`/`CTIME`），构造条件时引用常量避免硬编码。
