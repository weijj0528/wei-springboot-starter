# CLAUDE.md — wei-starter-swagger

> 本文件为模块文档主体；Codex 直接读取，Claude Code 经同目录 `CLAUDE.md` 的 `@` 导入读取。顶层架构见仓库根 `AGENTS.md`。

## 职责

集成 **Knife4j 4.5.0 + Springfox(Swagger2 3.0)**，提供接口文档与增强 UI。

## 包结构（`com.wei.starter.swagger`）

- `SwaggerProperties` — 配置属性，前缀 `swagger`
- `SwaggerAutoConfiguration` — 自动配置（`spring.factories` 注册）

## 配置示例

```yaml
swagger:
  enabled: true          # 生产环境务必设为 false
  version: 1.0
  description: 示例项目接口
  base-package: com.xxx  # 扫描包
  contact-name: William
  contact-url: http://xxx
  contact-email: xxx@xxx.com
```

## 关键约定

- 文档 UI 地址（Knife4j）：`/doc.html`。
- **线上环境通过 `swagger.enabled=false` 关闭**，避免接口暴露。
