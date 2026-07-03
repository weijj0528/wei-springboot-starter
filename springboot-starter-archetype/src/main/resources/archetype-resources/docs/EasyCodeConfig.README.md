# EasyCode 插件配置 - WeiStarter

基于 [EasyCode](https://plugins.jetbrains.com/plugin/14147-easycode) 插件，按本仓库规范一键生成 Entity / Mapper / Mapper.xml / Service / ServiceImpl / Controller / DTO。

## 配置文件

- [`EasyCodeConfig.json`](./EasyCodeConfig.json) — 完整配置（含模板分组 `WeiStarter`、全局配置、类型映射）

导入后会新增两个分组：

| 分组类型 | 分组名 | 说明 |
|---------|--------|------|
| Template | `WeiStarter` | 7 个代码模板 |
| Global Config | `WeiStarter` | 含 `init.vm` 审计字段排除逻辑 |
| Type Mapper | `Default` | 数据库类型 → Java 类型（沿用插件默认） |
| Column Config | `Default` | 列配置（沿用插件默认） |

## 生成的代码规范

| 模板 | 规范 |
|------|------|
| **entity.java.vm** | 继承 `Entity<Long>`，`@TableName` + `@Data` + `@ApiModel`，自动跳过审计字段 |
| **mapper.java.vm** | 继承 `XMapper<T>`（含 MyBatis-Plus `BaseMapper`），`@Mapper` |
| **mapper.xml.vm** | `BaseResultMap` + `Base_Column_List`，namespace 用 `.mapper.XxxMapper` |
| **service.java.vm** | 接口，定义 insertSelective/insertList/deleteByPrimaryKey/update/select/selectPageByExample |
| **serviceImpl.java.vm** | 继承 `AbstractService<T>`，`@Service`，实现 `getMapper()` |
| **controller.java.vm** | `@RestController` + 统一 `Result<T>` 响应 + Swagger `@Api` + 增删改查分页 |
| **dto.java.vm** | `@Data` + `@ApiModel`，字段与 Entity 一致 |

## 审计字段自动排除

`init.vm`（全局配置 `WeiStarter`）会自动从 `fullColumn` 排除 Entity 基类已声明的字段，避免重复声明：

`id` / `tenant` / `version` / `deleted` / `creator` / `updater` / `ctime` / `utime`

这些列在数据库表中仍需存在（由 `WeiMetaObjectHandler` 自动填充），只是 Entity/DTO 不重复声明。主键 `id` 保留在 `pkColumn`，用于 Mapper.xml 的 `where` 条件。

## 导入步骤

1. **安装插件**：IDEA → Settings → Plugins → 搜索 `EasyCode` → 安装重启
2. **导入配置**：Settings → Other Settings → EasyCode → 右上角 `Import` → 选择 `EasyCodeConfig.json`
3. **切换分组**：导入后 Template Group 选 `WeiStarter`，Global Config Group 选 `WeiStarter`
4. **设置作者**：Settings → Other Settings → EasyCode → Other → Author 填你的名字

## 使用方式

1. IDEA 的 Database 工具窗口连接数据库，选中要生成的表
2. 右键 → `EasyCode` → `Generate Code`
3. 分组选择 `WeiStarter`，包名填业务基础包（如 `com.example.demo`）
4. 选择要生成的模板，确认路径，生成

## 类型映射（Default）

| 数据库类型 | Java 类型 |
|-----------|-----------|
| `varchar` / `char` / `text` | `java.lang.String` |
| `bigint` | `java.lang.Long` |
| `int` / `tinyint` / `smallint` / `mediumint` | `java.lang.Integer` |
| `decimal` | `java.math.BigDecimal` |
| `date` / `datetime` / `timestamp` | `java.util.Date` |
| `time` | `java.time.LocalTime` |
| `boolean` / `bit` | `java.lang.Boolean` |

## 模板变量说明

| 变量 | 含义 |
|------|------|
| `$tableInfo.name` | 类名（由表名驼峰转换，去 `t_` 前缀） |
| `$tableInfo.obj.name` | 数据库表名 |
| `$tableInfo.comment` | 表注释 |
| `$tableInfo.savePackageName` | 用户填入的基础包名 |
| `$tableInfo.fullColumn` | 列集合（已排除审计字段） |
| `$tableInfo.pkColumn` | 主键列集合 |
| `$column.name` | 属性名（驼峰） |
| `$column.obj.name` | 数据库列名 |
| `$column.type` | Java 全限定类型 |
| `$column.ext.jdbcType` | JDBC 类型 |
| `$column.comment` | 列注释 |
| `$author` / `$time.currTime()` | 作者 / 当前时间 |
| `$tool.firstLowerCase()` / `$tool.firstUpperCase()` | 首字母小写/大写 |
| `$tool.getClsNameByFullName()` | 取类简名 |

## 与 springboot-starter-archetype 的区别

| | EasyCode | archetype |
|--|----------|-----------|
| 触发方式 | IDEA 插件，连数据库逆向 | Maven 命令，静态模板 |
| 输入 | 数据库表结构 | 命令行参数 |
| 输出 | 单表全套 CRUD 代码 | 整个项目骨架（多模块） |
| 用途 | 已有表，生成 CRUD 代码 | 新项目初始化 |

两者互补：archetype 建项目骨架，EasyCode 加表 CRUD。
