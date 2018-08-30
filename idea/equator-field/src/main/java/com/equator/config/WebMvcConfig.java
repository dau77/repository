package com.equator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring MVC相关配置
 */
@Configuration
//@EnableWebMvc //在Spring Boot中会有部分默认。如果想要完全控制MVC才需要
@EnableSwagger2
public class WebMvcConfig implements WebMvcConfigurer {


//    /**
//     * 拦截器
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        List<String> excludeList = new ArrayList<String>();
//        excludeList.add("/images/**");
//        excludeList.add("/");
//        excludeList.add("/error");
//        excludeList.add("/**.jpg");
//        excludeList.add("/**.css");
//        excludeList.add("/**.js");
//        registry.addInterceptor(new CommonHandlerInterceptor()).excludePathPatterns(excludeList);
//    }
}
