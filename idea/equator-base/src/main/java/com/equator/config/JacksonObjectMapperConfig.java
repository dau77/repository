package com.equator.config;

import com.equator.json.jackson.JsonAnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * jackson的ObjectMapper相关配置
 */
@Configuration
@ConditionalOnClass(ObjectMapper.class)
@ConditionalOnProperty(prefix = "equator.jackson.custom-filter-annotation", name = "enable", havingValue = "true", matchIfMissing = true)
public class JacksonObjectMapperConfig {

    /**
     * 使用JsonAnnotationIntrospector对象替换默认的对象。使用在原基础上增加获取@JSONFilter定义的过滤器ID
     * @param objectMapper
     */
    public JacksonObjectMapperConfig(ObjectMapper objectMapper) {
        objectMapper.setAnnotationIntrospector(new JsonAnnotationIntrospector());
    }
}
