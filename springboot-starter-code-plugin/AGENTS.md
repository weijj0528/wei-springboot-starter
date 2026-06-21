# CLAUDE.md — springboot-starter-code-plugin

> 本文件为模块文档主体；Codex 直接读取，Claude Code 经同目录 `CLAUDE.md` 的 `@` 导入读取。顶层架构见仓库根 `AGENTS.md`。

## 职责

Maven 插件（`packaging=maven-plugin`），基于 **MyBatis Generator + Freemarker** 从数据库表生成代码。

## 结构

- `CodeGeneratorMojo`（`AbstractMojo`）— 插件主类，组装 MyBatis Generator 的 `Context` 并附加 dto/service/controller 插件
- `src/main/resources/templates/` — Freemarker 模板：`controller.ftl`、`dto.ftl`、`service.ftl`、`serviceImpl.ftl`
- 默认读取业务项目的 `src/main/resources/generatorConfig.xml`

## 插件配置参数（`@Parameter`）

| 参数 | 说明 |
|------|------|
| `jdbcDriver`/`jdbcURL`/`jdbcUserId`/`jdbcPassword` | 数据库连接 |
| `basePackage` | 生成代码的基础包名 |
| `xMapper` | 通用 Mapper 父接口（如 `com.wei.starter.mybatis.xmapper.XMapper`） |
| `tableNames` | 目标表，默认 `all` |
| `noXml` | 无 XML 模式，不生成 Mapper XML |
| `generatorModle` | `base`(仅 MyBatis 相关) / `all`(额外生成 Dto/Service/ServiceImpl/Controller) |
| `swagger` | 是否生成 Swagger 注解，默认 true |
| `overwrite` | 是否覆盖已有文件，默认 true |

## 使用示例

```xml
<plugin>
    <groupId>com.github.weijj0528</groupId>
    <artifactId>springboot-starter-code-plugin</artifactId>
    <version>${revision}</version>
    <configuration>
        <jdbcURL>jdbc:mysql://localhost:3306/xxx</jdbcURL>
        <jdbcUserId>root</jdbcUserId>
        <jdbcPassword>***</jdbcPassword>
        <basePackage>com.xxx</basePackage>
        <xMapper>com.wei.starter.mybatis.xmapper.XMapper</xMapper>
        <noXml>true</noXml>
        <generatorModle>all</generatorModle>
    </configuration>
</plugin>
```

## 关键约定

- 修改生成产物的样式，改 `templates/*.ftl`，不要改 Mojo 里硬编码的字符串。
- 新增生成类型时，在 `addXxxPluginConfigurationToContext` 系列方法中扩展，并补充对应 `.ftl`。
