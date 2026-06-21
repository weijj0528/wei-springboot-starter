# CLAUDE.md — wei-starter-redis

> 本文件为模块文档主体；Codex 直接读取，Claude Code 经同目录 `CLAUDE.md` 的 `@` 导入读取。顶层架构见仓库根 `AGENTS.md`。

## 职责

Redis 综合 starter，包含三块独立能力（旧 `wei-starter-cache`、`wei-starter-lock` 已合并至此）：

1. **Redis 基础与原子操作** (`com.wei.starter.redis`)
2. **声明式缓存** (`com.wei.starter.cache`)
3. **声明式分布式锁** (`com.wei.starter.lock`)

## 自动配置

`spring.factories` 注册三个配置：`redis.RedisAutoConfig`、`cache.CacheAutoConfig`、`lock.LockAutoConfig`。

## 1. redis 包

- `RedisAutoConfig` — RedisTemplate 等基础配置
- `util/WeiRedisUtils` — Redis 操作工具
- `service` + `model` — 多 Key（hashKey）库存原子操作：`IRedisCommonStockService`/`RedisCommonStockServiceImpl`、`IRedisIncrService`，配套 `Stock*DTO`（init/modify/update）。用于库存扣减等需要原子性的场景。

## 2. cache 包（声明式缓存）

- `CacheAutoConfig` 关键常量：`REDIS_CACHE`、缓存管理器 `redisCacheManager`(`CM_REDIS`)、默认键生成器 `weiKeyGenerator`(`DEFAULT_KEY_GENERATOR`)
- `@RedisCacheable` — 增强版 `@Cacheable`，已绑定上述缓存管理器与键生成器，直接用于方法即可
- `@CacheTtl` + `CacheTtlAspect` + `CacheTtlContext` — 为单次缓存动态指定 TTL
- `WeiRedisCache`/`WeiRedisCacheManager`/`CacheService` — 自定义缓存实现

## 3. lock 包（声明式分布式锁）

- `@Lock` — 方法级加锁。属性：`value`/`lockName`(锁名，互为别名)、`key`(SpEL 锁键)、`expiredTime`(过期，默认 10s)、`waitTime`(等待，默认 0)
- `@Locking` — 容纳多个 `@Lock`，一次方法加多把锁
- `LockAspect` — 解析注解、执行加解锁的切面
- `WeiLock`（继承 JUC `Lock`）— 锁抽象；实现：`RedisLock`(原生 Redis)、`RedissonLock`(Redisson)、`MultipleLock`(多锁组合)
- `LockService` — 编程式加锁入口

## 关键约定

- 优先用注解 `@Lock`/`@RedisCacheable` 声明式使用；需要细粒度控制再用 `LockService`/`CacheService`。
- 锁键、缓存键使用 SpEL（基于方法参数）。
