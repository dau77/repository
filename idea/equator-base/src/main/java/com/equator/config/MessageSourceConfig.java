package com.equator.config;

import com.equator.context.support.DBMessageResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * MessageSource配置，改用数据库。如果ResourceBundleMessageSource的存在则作为父MessageSource
 */
@Configuration
@ConditionalOnProperty(prefix = "equator.db-messagesource", name = "enable", havingValue = "true", matchIfMissing = true)
public class MessageSourceConfig extends MessageSourceAutoConfiguration {

    @Autowired(required = false)
    ResourceBundleMessageSource parentMessageSource;


    @Bean
    @Override
    public MessageSource messageSource() {
        DBMessageResource messageSource = new DBMessageResource();
        //如果有配置消息资源文件，将其做为父源。DB的消息源取不到时，会在父源中查找
        if(parentMessageSource != null) {
            messageSource.setParentMessageSource(parentMessageSource);
        }
        return messageSource;
    }


    @Configuration
    @Conditional(ResourceBundleCondition.class)
    class ParentMessageSourceConfig extends MessageSourceAutoConfiguration {

        @Bean("parentMessageSource") //去掉name会在messageSource的bean后才创建？？
        @Override
        public ResourceBundleMessageSource messageSource() {
            return (ResourceBundleMessageSource) super.messageSource();
        }
    }
}
