# CLAUDE.md — springcloud-starter

> 本文件为模块文档主体；Codex 直接读取，Claude Code 经同目录 `CLAUDE.md` 的 `@` 导入读取。顶层架构见仓库根 `AGENTS.md`。

## 职责

面向 SpringCloud 微服务的聚合 starter：在 `springboot-starter` 基础上叠加云原生组件。

## 聚合内容

- `springboot-starter`（全部基础能力）
- `spring-cloud-starter-bootstrap`（bootstrap 配置引导）
- `spring-cloud-starter-alibaba-nacos-config`（Nacos 配置中心）
- `spring-cloud-starter-alibaba-nacos-discovery`（Nacos 注册/发现）
- SkyWalking：`apm-agent-core`、`apm-toolkit-logback-1.x`（链路追踪/日志）

## 使用

```xml
<dependency>
    <groupId>com.github.weijj0528</groupId>
    <artifactId>springcloud-starter</artifactId>
    <version>${revision}</version>
</dependency>
```

## 关键约定

- 纯聚合 POM，无源码。
- 引入即默认接入 Nacos，需在 `bootstrap.yml` 配置 Nacos server 地址等。
