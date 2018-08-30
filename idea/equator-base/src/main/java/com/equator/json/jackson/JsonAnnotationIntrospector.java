package com.equator.json.jackson;

//import com.fasterxml.jackson.annotation.JsonFilter;
import com.equator.json.JSONFilter;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class JsonAnnotationIntrospector extends JacksonAnnotationIntrospector {

    /**
     * 在原基础上增加通过@JSONFilter注解定义的过滤器ID
     * @param a
     * @return
     */
    @Override
    public Object findFilterId(Annotated a) {
        Object object = super.findFilterId(a);

        if(object != null) {
            return object;
        }

        JSONFilter ann = _findAnnotation(a, JSONFilter.class);
        if (ann != null) {
            String id = ann.value();
            // Empty String is same as not having annotation, to allow overrides
            if (id.length() > 0) {
                return id;
            }
        }
        return null;
    }

}
