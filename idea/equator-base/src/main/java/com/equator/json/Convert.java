package com.equator.json;

import com.equator.json.converter.ConvertType;
import com.equator.json.converter.NoneConverter;
import org.springframework.core.convert.converter.Converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Convert {

    String field();

    String target() default "";

    Class<? extends Converter> type() default NoneConverter.class;

    /**
     * 由ConvertType定义固定的转换类型，与type二选一即可，type优先
     * @return
     */
    ConvertType c() default ConvertType.NONE;
}
