package com.equator.controller;

import com.equator.json.Convert;
import com.equator.json.JSON;
import com.equator.json.JSONS;
import com.equator.json.jackson.JSONFilterProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * 处理@ResponseBody在序列化前,加上过滤器
 * 配合实体父类的@JSONFilter注解使用
 */
@ControllerAdvice
public class JSONResponseBodyAdvice extends AbstractMappingJacksonResponseBodyAdvice {


    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType, MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
        Annotation[] annos = returnType.getMethodAnnotations();
        JSONFilterProvider filterProvider = new JSONFilterProvider(); //如果bean定义了过滤器ID，但没有设置过滤器会抛异常
        Arrays.asList(annos).forEach(a -> { // 解析注解，设置过滤条件
            if (a instanceof JSON) {
                JSON json = (JSON) a;
                addFilter(filterProvider, json);
            } else if (a instanceof JSONS) { // 使用多重注解时，实际返回的是 @Repeatable(JSONS.class) 内指定的 @JSONS 注解
                JSONS jsons = (JSONS) a;
                Arrays.asList(jsons.value()).forEach(json -> {
                    addFilter(filterProvider, json);
                });
            }
        });
        bodyContainer.setFilters(filterProvider);
    }


    /**
     *
     * @param filterProvider FilterProvider
     * @param clazz target type
     * @param include include fields
     * @param exclude filter fields
     */
    private void addFilter(JSONFilterProvider filterProvider, Class<?> clazz, String include, String exclude, Convert[] converts) {
        if (clazz == null) return;
        if (StringUtils.isNotBlank(include)) {
            filterProvider.include(clazz, include.split(","));
        }
        if (StringUtils.isNotBlank(exclude)) {
            filterProvider.exclude(clazz, exclude.split(","));
        }
        if(converts != null && converts.length > 0) {
            filterProvider.setConverts(clazz, converts);
        }
    }


    private void addFilter(JSONFilterProvider filterProvider, JSON json) {
        this.addFilter(filterProvider, json.type(), json.include(), json.exclude(), json.converts());
    }


}
