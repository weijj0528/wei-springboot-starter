# 基础Starter

定义一套通用Bean、异常、校验分组，集成常用工具类，及相关配置与增强。

### 配置

##### JSON

Fastjson与Jackson配置

##### WEB

添加常用参数类型转换，添加

### 通用Bean

##### Header

请求头封装

```java
/**
 * 请求版本号
 */
private String version;
/**
 * 时间戳字符串
 */
private String time;
/**
 * 设备ID
 */
private String devices;
/**
 * 签名
 */
private String sign;

/**
 * 请求客户端类型
 */
private String model;

/**
 * 用户 请求 ip地址
 */
private String ip;

/**
 * Token
 */
private String token;
```

##### Page

分页参数封装

```java
/**
 * 分页数据，默认为空集
 */
private List<T> list=Collections.emptyList();

/**
 * 页码，从1开始
 */
private int page=1;

/**
 * 页容量，默认为20
 */
private int size=20;

/**
 * 总数默认为0
 */
private long total=0;
```

##### Range

范围参数封装

```java
/**
 * 开始
 */
private T from;

/**
 * 结束
 */
private T to;
```

##### Result

请求结果封装

```java
/**
 * 错误编号
 */
private String code;
/**
 * 错误信息
 */
private String msg;
/**
 * 业务数据
 */
private T data;
```

### 异常架构

> 见 com.wei.starter.base.exception 包 <br>
> com.wei.starter.base.advice.GlobalExceptionHandler 通用异常处理

### 校验分组

Add：增加<br>
Save：保存<br>
Del：删除<br>
Remove：移除<br>
Update：更新<br>
Edit：编辑<br>
Query：查询<br>
Select：选择<br>
Close：关闭<br>
Open：开启<br>
Apply：申请<br>
Audit：审核<br>

### 工具类

> com.wei.starter.base.util 包

WeiBeanUtil：Bean转换<br>
WeiObjUtils：对象处理<br>
WeiWebUtils：Web工具<br>

### 其他

##### 日志记录

##### PV标记