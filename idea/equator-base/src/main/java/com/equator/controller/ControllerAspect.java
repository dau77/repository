package com.equator.controller;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 控制器切面。暂时时用于调试
 */
@ConditionalOnProperty(prefix = "equator.aspect.controller", name = "enable", havingValue = "true")
@Aspect
@Component
public class ControllerAspect {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 定义拦截规则：拦截com.gmaple.springdemo.controller包(包括子孙包)下面的所有类中，有@GetMapping或@PostMapping注解的方法。
     */
//    @Pointcut("execution(* com.gmaple.springdemo.controller..*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
//    @Pointcut("execution(* com.gmaple.springdemo.controller..*(..))")
    @Pointcut("within(com.equator.controller..*) && " +
            "(@annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping))")
    public void controllerMethodPointcut(){}

    @Around("controllerMethodPointcut()") // //指定拦截器规则；也可以直接把“execution(......)”写进这里
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Object model = null;

        logger.info("=======> before request："); //上传文件的可能还要另写
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request2 = ((ServletRequestAttributes)requestAttributes).getRequest();
        Enumeration<String> enumeration = request2.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String paramName = enumeration.nextElement();

            logger.info("=======> before request[{}]:{}", paramName, request2.getParameter(paramName));
        }

        logger.info("=======> before 请求参数：");
        for(int i=0; i<args.length; i++) {
            Object arg = args[i];
            logger.info("[{}]{}", i, arg);
            if(Model.class.isInstance(arg)) {
                model = arg;
            }
        }

        // start stopwatch
        Object retVal = pjp.proceed();
        // stop stopwatch
        logger.info("=======> after 返回值:{}", retVal);
        if (model != null) {
            logger.info("=======> after Model:{}", model);
        }
        //或再把请求参数再输出一次？
        return retVal;
    }
}
