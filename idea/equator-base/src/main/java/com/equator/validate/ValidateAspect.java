package com.equator.validate;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ValidateAspect {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("execution(public void org.springframework.validation.beanvalidation.SpringValidatorAdapter.validate(Object,org.springframework.validation.Errors,*))")
    public void validateMethodPointcut(){}

    @Around("validateMethodPointcut()") // //指定拦截器规则；也可以直接把“execution(......)”写进这里
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        ValidateGroupHolder.set((Object[]) pjp.getArgs()[2]);
        Object retVal = null;
        try {
            retVal = pjp.proceed();
        } finally {
            ValidateGroupHolder.remove();
        }
        return retVal;
    }

    public static Object[] getGroups() {
        return ValidateGroupHolder.get();
    }
}