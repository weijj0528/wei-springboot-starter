package com.wei.starter.mybatis.plugin;

import cn.hutool.core.bean.BeanUtil;
import com.wei.starter.base.util.WeiBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.Properties;

/**
 * The type Args interceptor.
 * 附加额外参数插件
 * 注意：不修改改 Sql
 *
 * @author William.Wei
 */
@Slf4j
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class ArgsInterceptor implements Interceptor {

    private final ArgsProvider argsProvider;

    /**
     * Instantiates a new Args interceptor.
     *
     * @param argsProvider the args provider
     */
    public ArgsInterceptor(@NotNull ArgsProvider argsProvider) {
        this.argsProvider = argsProvider;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        // 获取 SQL 命令
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        // 获取参数
        Object parameter = invocation.getArgs()[1];
        Object source;
        switch (sqlCommandType) {
            case INSERT:
                source = argsProvider.insertArgs();
                break;
            case UPDATE:
                source = argsProvider.updateArgs();
                break;
            case SELECT:
                source = argsProvider.selectArgs();
                break;
            default:
                source = null;
        }
        // 属性复制
        try {
            Optional.ofNullable(source).ifPresent(s -> BeanUtil.copyProperties(s, parameter, WeiBeanUtil.getNullPropertyNames(s)));
        } catch (Exception e) {
            log.warn("ArgsInterceptor copy properties error!", e);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}