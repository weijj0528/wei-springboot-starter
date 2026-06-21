# CLAUDE.md — springboot-starter

> 本文件为模块文档主体；Codex 直接读取，Claude Code 经同目录 `CLAUDE.md` 的 `@` 导入读取。顶层架构见仓库根 `AGENTS.md`。

## 职责

聚合 starter，业务项目只需引入它即可获得全部基础能力。

## 聚合内容

依赖并传递：`wei-starter-base`、`wei-starter-mybatis`、`wei-starter-redis`、`wei-starter-swagger`（均为 `${revision}` 版本）。

> 不含 `wei-starter-sequence`（按需单独引入）。

## 使用

```xml
<dependency>
    <groupId>com.github.weijj0528</groupId>
    <artifactId>springboot-starter</artifactId>
    <version>${revision}</version>
</dependency>
```

## 关键约定

- 这是一个纯聚合 POM，无源码。新增/移除默认引入的 starter 在此模块 `pom.xml` 调整。
- SpringCloud 场景请改用 `springcloud-starter`（它已包含本模块）。
