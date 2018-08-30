package com.equator.json.converter;

import org.springframework.core.convert.converter.Converter;

public class NoneConverter implements Converter<Object, String> {
    @Override
    public String convert(Object source) {
        return null;
    }
}
