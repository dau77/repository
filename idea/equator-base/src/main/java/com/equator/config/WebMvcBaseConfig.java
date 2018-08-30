package com.equator.config;

import com.equator.converter.DateConverter;
import com.equator.web.SnakeToCamelModelAttributeMethodProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring MVC相关配置
 */
@Configuration
//@EnableWebMvc //在Spring Boot中会有部分默认。如果想要完全控制MVC才需要
@ConditionalOnProperty(prefix = "equator.mvc.config.base", name = "enable", havingValue = "true", matchIfMissing = true)
public class WebMvcBaseConfig implements WebMvcConfigurer {


    private MessageSource messageSource;

    @Autowired
    public WebMvcBaseConfig(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        //ValidationMessage与Spring的message使用同一个消息源配置文件，不再使用ValidationMessages.properties
        localValidatorFactoryBean.setValidationMessageSource(messageSource);
        return localValidatorFactoryBean;
    }

    @Override
    public Validator getValidator() {
        return validator();
    }

    /**
     * 添加解析器来支持自定义控制器方法参数类型。
     * 这不会覆盖解析处理程序方法参数的内置支持。为参数解析定制内置的支持。
     *
     * 可以通过这个，自定义返回的对象
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(new SnakeToCamelHandlerMethodArgumentResolver());
//        resolvers.add(new SnakeToCamelModelAttributeMethodProcessor(true));
        resolvers.add(snakeToCamelModelAttributeMethodProcessor);
    }

    @Autowired
    private SnakeToCamelModelAttributeMethodProcessor snakeToCamelModelAttributeMethodProcessor;

    /**
     * 定义的是全局；另一种可以在ControllerAdvice中@InitBinder的方法中registerCustomEditor处理。ControllerAdvice的可以定义作用在哪些包或类？
     * 这种方式好像是验证完再转？而在registerCustomEditor是处理完就验证？待测试 ModelAttributeMethodProcessor.resolveArgument()
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        //String时间戳转Date
        registry.addConverter(new DateConverter());
    }

}
