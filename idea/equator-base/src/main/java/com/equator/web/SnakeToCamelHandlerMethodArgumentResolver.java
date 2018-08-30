package com.equator.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 如果验证需要验证,不能使用这个处理.因为处理BindingResult会取前一个(String lastKey = CollectionUtils.lastElement(model.keySet()); model.get(lastKey)).
 * 如果要优化可参考ModelAttributeMethodProcessor类的处理
 */
public class SnakeToCamelHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //return methodParameter.hasParameterAnnotation(ParamModel.class); 通过自定义注解匹配
        // return arg0.getParameterType().equals(UserInfo.class); //通过类型匹配
        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        Object obj = BeanUtils.instantiate(parameter.getParameterType());
        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(obj);
        Iterator<String> paramNames = webRequest.getParameterNames();
        while (paramNames.hasNext()) {
            String paramName = paramNames.next();
            Object o = webRequest.getParameter(paramName);
            try {
                wrapper.setPropertyValue(snakeToCamel(paramName), o);
            } catch (Exception e) {
                logger.warn("------->", e);
            }
        }


        return obj;
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
