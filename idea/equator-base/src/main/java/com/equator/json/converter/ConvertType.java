package com.equator.json.converter;

import org.springframework.core.convert.converter.Converter;

public enum ConvertType {

    NONE(NoneConverter.class),

    SystemConfigValueToName(SystemConfigConverter.class);

    private Class<? extends Converter> converter;

    ConvertType(Class<? extends Converter> converter) {
        this.converter = converter;
    }

    public Class<? extends Converter> getConverter() {
        return converter;
    }
}
