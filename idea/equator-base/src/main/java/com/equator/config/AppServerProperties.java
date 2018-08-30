package com.equator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 服务器相关配置(自定义)
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.server")
public class AppServerProperties {

    public static final String NAME_ID = "server_id";

    /**
     * 服务器ID标识
     */
    private String id;
}
