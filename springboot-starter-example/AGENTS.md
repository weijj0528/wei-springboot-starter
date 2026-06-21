# CLAUDE.md — springboot-starter-example

> 本文件为模块文档主体；Codex 直接读取，Claude Code 经同目录 `CLAUDE.md` 的 `@` 导入读取。顶层架构见仓库根 `AGENTS.md`。

## 职责

各 starter 的可运行示例集合，用于验证功能与作为接入参考。

## 子模块与启动类

| 子模块 | 启动类 | 演示 |
|--------|--------|------|
| `springboot-starter-example-base` | `BaseExampleApplication` | 通用 Bean/异常/校验/工具 |
| `springboot-starter-example-mybatis` | `MybatisExampleApplication` | MyBatis-Plus 实体/Service/Mapper |
| `springboot-starter-example-redis` | `RedisExampleApplication` | 缓存/锁/原子操作 |
| `springboot-starter-example-sequence` | `SequenceExampleApplication` | 双缓冲序号 |
| `springboot-starter-example-swagger` | `Swagger2ExampleApplication` | 接口文档 UI |

- 示例包名：`com.github.weijj0528.example.*`。

## 测试

```bash
# 跑某示例全部测试
mvn test -pl springboot-starter-example/springboot-starter-example-mybatis

# 跑单个测试类
mvn test -pl springboot-starter-example/springboot-starter-example-mybatis -Dtest=UserAuthPhoneServiceTest
```

## 关键约定

- 修改 `wei-starter-*` 源码后，需先 `mvn install` 对应模块，示例才能取到改动。
- 新增示例：以现有 `mybatis` 模块为模板（含 model/dto/mapper/service/impl/controller 与测试）。
