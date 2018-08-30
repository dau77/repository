package com.equator.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.equator.persistence.mybatisplus.CustomSqlInjector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatisPlus配置
 */
@Configuration
public class MybatisPlusConfig {
    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 自定义sql注入器
     * MybatisPlusAutoConfiguration.sqlSessionFactory会获取并注入globalConfig。类似的还有：主键生成器、填充器
     * @return
     */
    @ConditionalOnProperty(prefix = "equator.mp.sqlinjector.custom", name = "enable", havingValue = "true", matchIfMissing = true)
    @Bean
    public ISqlInjector getISqlInjector() {
        return new CustomSqlInjector();
    }
}
