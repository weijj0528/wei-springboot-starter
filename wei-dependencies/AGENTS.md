# CLAUDE.md — wei-dependencies

> 本文件为模块文档主体；Codex 直接读取，Claude Code 经同目录 `CLAUDE.md` 的 `@` 导入读取。顶层架构见仓库根 `AGENTS.md`。

## 职责

依赖版本管理 BOM（`packaging=pom`），统一约束全仓所有第三方依赖版本，被各模块 `import`。

## 关键约定

- **所有第三方依赖的版本只在此声明**；子模块引依赖时不写 `<version>`。
- 版本属性集中在 `<properties>`：`spring-boot.version=2.7.18`、`spring-cloud.version=2021.0.9`、`spring-cloud-alibaba.version=2021.0.6.0`、`mapstruct.version=1.6.3`、`lombok.version=1.18.38` 等。
- import 了三个上游 BOM：spring-boot-dependencies、spring-cloud-dependencies、spring-cloud-alibaba-dependencies。
- 升级依赖：改本文件对应属性/版本号；升级框架大版本需同步评估各 starter 兼容性（尤其 `spring.factories` → 3.x 的 `.imports` 迁移）。
- 自身版本同样用 `${revision}`，经 `flatten-maven-plugin` 扁平化。
