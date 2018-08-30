/**
 * @(#)JsonHandler.java   1.00 2012/07/24
 * Copyright (c) 2012 深圳市枫叶软件公司.
 * All rights reserved.
 */
package com.equator.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

/**
 * 封装了操作JSON的方法,使用JSON库为jackson
 * @author nemo
 *
 */
public class JsonHandler{
    private static ObjectMapper _objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

    /**
     * 将JSON字串恢复为对象
     * @author nemo
     * @param <T>
     * @param jsonStr JSON字串
     * @param object pojo类
     * @return T pojo对象
     */
    public static <T> T json2Object(String jsonStr, Class<T> object) throws IOException {
        return _objectMapper.readValue(jsonStr, object);
    }
    
    /**
     * 将对象封装为JSON字串
     * @author nemo
     * @param object pojo对象
     * @return JSON 字串
     */
    public static String object2Json(Object object) throws JsonProcessingException {
        return _objectMapper.writeValueAsString(object);
    }

}
