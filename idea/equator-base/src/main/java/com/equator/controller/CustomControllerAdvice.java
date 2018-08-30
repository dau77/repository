package com.equator.controller;

import com.equator.exception.BindResultException;
import com.equator.exception.ErrorException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;

/**
 * ControllerAdvice全局配置
 * 定义了如：Controller异常的处理器
 */
@ControllerAdvice //可以定义作用域
public class CustomControllerAdvice {

    /**
     *     另一种方法可以MVC配置类中，重写addFormatters(FormatterRegistry registry)方法，addConverter添加一个转换器实现
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder){
//        binder.registerCustomEditor(Date.class, new DateEditor());
    }


    @ExceptionHandler(BindResultException.class)
    public String handleBindResultException(Exception e, HttpServletRequest request) {
        request.setAttribute("javax.servlet.error.status_code", 400);

        BindResultException bindResultException = (BindResultException) e;
        request.setAttribute("resultModel", bindResultException.getResult());
        return "forward:/error";
    }

    /**
     * 自定的异常人处理。该通常在业务处理过程中出错抛出
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(ErrorException.class)
    public String handleErrorException(Exception e, HttpServletRequest request) {
        request.setAttribute("javax.servlet.error.status_code", 422);

        ErrorException errorException = (ErrorException) e;
        request.setAttribute("resultModel", errorException.getResult());
        return "forward:/error";
    }

    /**
     * 控制器参数校验失败抛出异常的处理（没有写BindingResult参数时）
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        BindResultException bindResultException = new BindResultException(e.getBindingResult());
        return handleBindResultException(bindResultException, request);
    }

}
