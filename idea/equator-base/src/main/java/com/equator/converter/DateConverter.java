package com.equator.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * 用于将时间戳的字符串，转换成Date对象
 */
public class DateConverter implements Converter<String, Date> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Date convert(String s) {
        if ("".equals(s) || s == null) {
            return null;
        }
        try {
            return new Date(Long.valueOf(s));
        } catch (Exception e) {
            logger.warn("String (long) to Date", e);
        }
        return null;
    }
}
