# CLAUDE.md — wei-starter-sequence

> 本文件为模块文档主体；Codex 直接读取，Claude Code 经同目录 `CLAUDE.md` 的 `@` 导入读取。顶层架构见仓库根 `AGENTS.md`。

## 职责

基于数据库的**双缓冲（Double Buffer）序号生成**，在高并发下减少 DB 访问、避免取号阻塞。

## 包结构（`com.wei.starter.sequence`）

- `SequenceAutoConfig` — 自动配置（`spring.factories` 注册）
- `DoubleBufferSequence` — 核心：双缓冲取号，`getAndIncrement(sysName, bizKey)` 为线程安全取号入口；一个缓冲区耗尽时切换到另一个并异步预取
- `DbSequence` / `RedisSequence` — 基于 DB / Redis 的序号实现
- `incr/` — 号段抽象：`Space`/`SequenceSpace`/`DbSequenceSpace`、`SpaceFactory`（号段加载与生产工厂）

## 关键约定

- 取号按 `(sysName, bizKey)` 维度隔离，不同业务键互不影响。
- 双缓冲：维护两个 `SequenceSpace`，当前段消费到阈值时由 `SpaceFactory` 预取下一段，实现平滑切换。
- 修改取号逻辑务必保证 `getAndIncrement` 的线程安全（现为 `synchronized`）。
