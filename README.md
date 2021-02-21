# SpringBoot项目基础Starter
### 提供常用的基础配置与代码生成插件
1. springboot-starter 集成Starter，包含各wei-starter模块

2. springboot-starter-code-plugin 代码生成Maven插件

3. springboot-starter-example 示例项目

4. wei-starter-base 基础Starter项目，包含通用Bean、异常架构、工具类集成、校验分组及相关配置

5. wei-starter-cache 缓存实践，集成Redis缓存

6. wei-starter-lock 基于Redis实现类似于声明式缓存式的声明式分布式锁

7. wei-starter-mybatis 集成Mybatis，集成通用Mapper与通用分页插件

8. wei-starter-redis 集成Redis，提供多Key[hashKey]原子操作

8. wei-starter-security 安全实践，抽象基于Token的安全策略

8. wei-starter-sequence 序号生成，抽象基于数据库的双缓冲序号生成

8. wei-starter-swagger2 Swagger2集成配置

### springboot-starter介绍

- 工具类集成
```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.9</version>
</dependency>
<dependency>
    <groupId>commons-fileupload</groupId>
    <artifactId>commons-fileupload</artifactId>
    <version>1.4</version>
</dependency>
<dependency>
    <groupId>commons-codec</groupId>
    <artifactId>commons-codec</artifactId>
    <version>1.12</version>
</dependency>
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-collections4</artifactId>
    <version>4.4</version>
</dependency>
<dependency>
    <groupId>commons-beanutils</groupId>
    <artifactId>commons-beanutils</artifactId>
    <version>1.9.3</version>
</dependency>
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
    <version>2.6.2</version>
</dependency>
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.9</version>
</dependency>
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>4.5.1</version>
</dependency>
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.56</version>
</dependency>
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>27.0.1-jre</version>
</dependency>
```
工具类使用请自行查看相关文档
- Swagger2集成
```properties
#是否开启，生产环境请设置为false
swagger.enabled=true
#版本
swagger.version=1.0
#说明
swagger.description=示例项目接口
#扫描的包
swagger.base-package=com.wei.springbootstarterexample
#联系人
swagger.contact-name=William
swagger.contact-url=http://xxx.xxx.xxx
swagger.contact-email=William.Wei@g-town.com.cn
```
- 自动配置（缓存，Fastjson，Redis，SpringMvc）
- 锁封装（RedisLock）
- 异常架构（全局异常处理）
- 基础（bean,dto,service）
- 日志打印，添加MDC日志链路追踪
- 基于flyway的数据库版本控制

### springboot-starter-code-plugin使用
```xml
<plugin>
    <groupId>com.wei</groupId>
    <artifactId>springboot-starter-code-plugin</artifactId>
    <version>2.0</version>
    <configuration>
        <jdbcDriver>com.mysql.jdbc.Driver</jdbcDriver>
        <jdbcURL>jdbc:mysql://localhost:3306/wei_com</jdbcURL>
        <jdbcUserId>root</jdbcUserId>
        <jdbcPassword>******</jdbcPassword>
        <!--项目基础包名-->
        <basePackage>com.wei.springbootstarterexample</basePackage>
        <!--通用Mapper继承类-->
        <xMapper>com.wei.springboot.starter.mybatis.XMapper</xMapper>
        <!--无XML模式，不生成XML文件-->
        <noXml>true</noXml>
        <!--生成模式，base/all base模式只生成Mybatis相关，all模式额外再生成Dto/Service/ServiceImpl-->
        <generatorModle>all</generatorModle>
    </configuration>
</plugin>
```

