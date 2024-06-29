package com.wei.starter.mybatis.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.wei.starter.base.config.EnvConfig;
import com.wei.starter.mybatis.plugin.SqlCostInterceptor;
import com.wei.starter.mybatis.plus.WeiMetaObjectHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * The type Mybatis plus config.
 *
 * @author Weijj0528
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 默认插件配置
     *
     * @return the mybatis plus interceptor
     */
    @Bean
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        // 添加非法SQL拦截器
        interceptor.addInnerInterceptor(new IllegalSQLInnerInterceptor());
        // 防全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }

    @Bean
    @ConditionalOnMissingBean(MetaObjectHandler.class)
    public MetaObjectHandler metaObjectHandler() {
        return new WeiMetaObjectHandler();
    }

    /**
     * 非生产环境开启SQL打印耗时统计
     *
     * @return the sql cost interceptor
     */
    @Bean
    @Profile({EnvConfig.DEV, EnvConfig.TEST, EnvConfig.UAT})
    public SqlCostInterceptor sqlCostInterceptor() {
        return new SqlCostInterceptor();
    }

}
