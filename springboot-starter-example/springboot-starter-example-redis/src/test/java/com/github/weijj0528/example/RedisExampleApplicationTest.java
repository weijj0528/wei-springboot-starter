package com.github.weijj0528.example;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Redis 示例集成测试基类：基于 Testcontainers 启动真实 Redis 容器。
 * <p>
 * 采用共享单例容器模式（{@link #REDIS} 为 {@code static} 且手动 start），
 * 多个测试子类复用同一容器，避免每个子类各起容器导致的上下文复用连接失效问题。
 * 经 {@link DynamicPropertySource} 覆盖 application-dev.yml 中的外部 Redis 配置，
 * 使所有继承本类的测试无需外部 Redis 服务即可运行。
 * <p>
 * CI 环境仅需 Docker。
 *
 * @author William.Wei
 */
@SpringBootTest(classes = RedisExampleApplication.class)
@ActiveProfiles("test")
@Import(RedisTestConfig.class)
public class RedisExampleApplicationTest {

    private static final DockerImageName REDIS_IMAGE = DockerImageName.parse("redis:7-alpine");

    /**
     * 共享单例容器：所有子类复用，JVM 退出时由 Testcontainers 关闭钩子回收。
     */
    @SuppressWarnings("resource")
    private static final GenericContainer<?> REDIS = new GenericContainer<>(REDIS_IMAGE)
            .withExposedPorts(6379)
            .withReuse(true);

    static {
        REDIS.start();
    }

    /**
     * 将容器映射的 host/port 注入 spring.redis，覆盖外部 Redis 配置。
     * redisson.config 在 application-test.yml 中清空密码并指向同一容器地址。
     */
    @DynamicPropertySource
    static void redisProps(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", REDIS::getHost);
        registry.add("spring.redis.port", () -> REDIS.getMappedPort(6379));
    }
}
