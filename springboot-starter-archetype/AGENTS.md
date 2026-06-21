# CLAUDE.md — springboot-starter-archetype

> 本文件为模块文档主体；Codex 直接读取，Claude Code 经同目录 `CLAUDE.md` 的 `@` 导入读取。顶层架构见仓库根 `AGENTS.md`。

## 职责

Maven 原型（archetype），一键生成已集成本 starter 的**三层多模块业务骨架**。

## 生成的项目结构

```
${artifactId}/
├── ${artifactId}-api      # 对外接口与 DTO（api/、dto/）
├── ${artifactId}-service  # 业务实现：entity、mapper(+XML)、service(+impl)、controller(admin/api)、dto
└── ${artifactId}-web      # 启动模块：Application、application*.yml、Flyway 迁移、测试
```

- 模板源码在 `src/main/resources/archetype-resources/`，模块与过滤规则定义在 `src/main/resources/META-INF/maven/archetype-metadata.xml`。
- `__rootArtifactId__-*` 目录名会被替换为实际 `artifactId`。
- 含示例领域对象 `Hello`（Entity/DTO/Mapper/Service/Controller + 测试），生成后可直接运行参考。

## 原型必填属性

`groupId`(默认 `com.wei.saas`)、`artifactId`、`version`(默认 `1.0.0-SNAPSHOT`)、`package`。

## 生成命令

```bash
mvn archetype:generate \
  -DarchetypeGroupId=com.github.weijj0528 \
  -DarchetypeArtifactId=springboot-starter-archetype \
  -DarchetypeVersion=4.1.0-SNAPSHOT \
  -DgroupId=com.example -DartifactId=my-app -Dpackage=com.example.myapp
```

## 关键约定

- 生成项目默认用 [EasyCode IDEA 插件](https://plugins.jetbrains.com/plugin/10954-easycode) + 根目录 `EasyCodeConfig.json` 做后续代码生成。
- 修改骨架时编辑 `archetype-resources/` 下模板；新增需要变量替换的文件要在 `archetype-metadata.xml` 的 fileSet 中声明 `filtered="true"`。
