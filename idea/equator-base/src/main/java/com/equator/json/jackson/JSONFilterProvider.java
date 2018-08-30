package com.equator.json.jackson;

import com.equator.json.Convert;
import com.equator.json.converter.ConvertType;
import com.equator.json.converter.NoneConverter;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.*;

/**
 * JSON注解对应的FilterProvider
 */
//@SuppressWarnings("deprecation")
@JsonFilter("JacksonFilter")
public class JSONFilterProvider extends FilterProvider {

    private Map<Class<?>, Map<String, Class<? extends Converter>>> convertMap = new HashMap<>();

    private Map<Class<?>, Map<String, String>> convertFieldMap = new HashMap<>();

    private Map<Class<?>, Set<String>> includeMap = new HashMap<>();
    private Map<Class<?>, Set<String>> excludeMap = new HashMap<>();

    public void include(Class<?> type, String[] fields) {
        addToMap(includeMap, type, fields);
    }

    public void exclude(Class<?> type, String[] fields) {
        addToMap(excludeMap, type, fields);
    }

    public void setConverts(Class<?> type, Convert[] converts) {
        for(Convert convert : converts) {
            Class<? extends Converter> clazz = parseCconverterClass(convert);
            if(clazz != null) {
                addToMap(includeMap, type, convert.field());

                addToConvertMap(type, convert.field(), clazz, convert.target());
            }
        }
    }

    private Class<? extends Converter> parseCconverterClass(Convert convert) {
        if(!NoneConverter.class.equals(convert.type())) {
            return convert.type();
        }
        else if(convert.c() != ConvertType.NONE) {
            return convert.c().getConverter();
        }
        return null;
    }

    private void addToConvertMap(Class<?> type, String field, Class<? extends Converter> convertClass, String target) {
        if(!convertMap.containsKey(type)) {
            convertMap.put(type, new HashMap<>());
            convertFieldMap.put(type, new HashMap<>());
        }
        convertMap.get(type).put(field, convertClass);
        convertFieldMap.get(type).put(field, StringUtils.isEmpty(target) ? field : target);
    }

    private void addToMap(Map<Class<?>, Set<String>> map, Class<?> type, String... fields) {
        Set<String> fieldSet = map.getOrDefault(type, new HashSet<>());
        fieldSet.addAll(Arrays.asList(fields));
        map.put(type, fieldSet);
    }





    @Override
    public BeanPropertyFilter findFilter(Object filterId) {
        throw new UnsupportedOperationException("Access to deprecated filters not supported");
    }

    @Override
    public PropertyFilter findPropertyFilter(Object filterId, Object valueToFilter) {

        return new SimpleBeanPropertyFilter() {

            @Override
            public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider prov, PropertyWriter writer)
                    throws Exception {
                if (apply(pojo.getClass(), writer.getName())) {
//                    jgen.writeStringField("hehe", "haro");
                    writer.serializeAsField(pojo, jgen, prov);

                    //
                    Class<? extends Converter> convertType = convertType(pojo.getClass(), writer.getName());
                    if(convertType != null) {
                        serializeAsConvert(convertType, pojo, jgen, prov, writer);
//                        Convert convert = BeanUtils.instantiateClass(convertType);
//                        convert.exec(pojo, jgen, prov, writer, convertTargetField(pojo.getClass(), writer.getName()));
                    }
                } else if (!jgen.canOmitFields()) {
                    writer.serializeAsOmittedField(pojo, jgen, prov);
                }
            }
        };
    }

    private void serializeAsConvert(Class<? extends Converter> convertType, Object pojo, JsonGenerator jgen,
                                    SerializerProvider prov, PropertyWriter writer) throws Exception {

        String targetField = convertTargetField(pojo.getClass(), writer.getName());
        if(targetField == null) {
            return;
        }

//        BeanPropertyWriter beanPropertyWriter = (BeanPropertyWriter) writer;
//        Object val = beanPropertyWriter.get(pojo);

        Converter<Object, String> converter = BeanUtils.instantiateClass(convertType); //TODO 是否能从spring中取？或自己内存？
        String target = converter.convert(pojo);

        //传对象 如果对象间没有继承关系，但是要做的转换类似，就需要建2个converter
        //传参数 不能用converter接口，需要另传其它参数，如system_config.category
        //传MAP?


        if(target != null) {
            jgen.writeStringField(targetField, target); //其它类型？
        }
    }

    private boolean apply(Class<?> type, String name) {
        Set<String> includeFields = includeMap.get(type);
        Set<String> excludeFields = excludeMap.get(type);
        if (includeFields != null && includeFields.contains(name)) {
            return true;
        } else if (excludeFields != null && !excludeFields.contains(name)) {
            return true;
        } else if (includeFields == null && excludeFields == null) {
            return true;
        }
        return false;
    }

    private Class<? extends Converter> convertType(Class<?> type, String name) {
        Map<String, Class<? extends Converter>> map = convertMap.get(type);
        if(map != null && map.containsKey(name)) {
            return map.get(name);
        }
        return null;
    }

    private String convertTargetField(Class<?> type, String name) {
        Map<String, String> map = convertFieldMap.get(type);
        if(map != null && map.containsKey(name)) {
            return map.get(name);
        }
        return null;
    }
}
