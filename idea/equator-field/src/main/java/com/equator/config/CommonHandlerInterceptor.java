package com.equator.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器。暂无实际作用
 */
public class CommonHandlerInterceptor implements HandlerInterceptor {

    Logger logger = LoggerFactory.getLogger(getClass());

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.warn("拦截器preHandle:handler==>{}", handler);
        logger.warn(request.getRequestURI());
        return true;
    }
}
