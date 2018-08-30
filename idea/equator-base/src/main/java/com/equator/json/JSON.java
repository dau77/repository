package com.equator.json;

import java.lang.annotation.*;

/**
 * 主要用于在REST控制器方法中定义响应的字段过滤和转换。一般配合@JSONFilter使用
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(JSONS.class)   // 让方法支持多重@JSON 注解
public @interface JSON {

    Class<?> type();

    String include() default "";

    String exclude() default "";

    Convert[] converts() default {};
}