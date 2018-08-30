package com.equator.web;

//import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import javax.servlet.ServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SnakeToCamelModelAttributeMethodProcessor extends ServletModelAttributeMethodProcessor implements ApplicationContextAware {

    ApplicationContext applicationContext;

    public SnakeToCamelModelAttributeMethodProcessor() {
        super(true);
    }
//
//    public SnakeToCamelModelAttributeMethodProcessor(boolean annotationNotRequired) {
//        super(annotationNotRequired);
//    }


    /**
     * 实现ApplicationContextAware接口，并重写setApplicationContext(ApplicationContext applicationContext)方法，在工具类中使用@Component注解让spring进行管理。
     * spring容器在启动的时候，会调用setApplicationContext()方法将ApplicationContext 对象设置进去。
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
        SnakeToCamelRequestDataBinder camelBinder = new SnakeToCamelRequestDataBinder(binder.getTarget(), binder.getObjectName());
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = applicationContext.getBean(RequestMappingHandlerAdapter.class);
        requestMappingHandlerAdapter.getWebBindingInitializer().initBinder(camelBinder, request);
        camelBinder.bind(request.getNativeRequest(ServletRequest.class));
    }


    public class SnakeToCamelRequestDataBinder extends ExtendedServletRequestDataBinder {

        public SnakeToCamelRequestDataBinder(Object target, String objectName) {
            super(target, objectName);
        }

        protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {
            super.addBindValues(mpvs, request);

            //处理JsonProperty注释的对象
//            Class<?> targetClass = getTarget().getClass();
//            Field[] fields = targetClass.getDeclaredFields();
//            for (Field field : fields) {
//                JsonProperty jsonPropertyAnnotation = field.getAnnotation(JsonProperty.class);
//                if (jsonPropertyAnnotation != null && mpvs.contains(jsonPropertyAnnotation.value())) {
//                    if (!mpvs.contains(field.getName())) {
//                        mpvs.add(field.getName(), mpvs.getPropertyValue(jsonPropertyAnnotation.value()).getValue());
//                    }
//                }
//            }

            List<PropertyValue> covertValues = new ArrayList<PropertyValue>();
            for (PropertyValue propertyValue : mpvs.getPropertyValueList()) {
                if(propertyValue.getName().contains("_")) {
                    String camelName = snakeToCamel(propertyValue.getName());
                    if (!mpvs.contains(camelName)) {
                        covertValues.add(new PropertyValue(camelName, propertyValue.getValue()));
                    }
                }
            }
            mpvs.getPropertyValueList().addAll(covertValues);
        }

    }


    /**
     * 匹配下划线的格式
     */
    private static Pattern pattern = Pattern.compile("_(\\w)");

    private static String snakeToCamel(String source) {
        Matcher matcher = pattern.matcher(source);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
