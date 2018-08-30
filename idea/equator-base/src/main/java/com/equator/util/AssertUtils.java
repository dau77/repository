package com.equator.util;

import com.equator.exception.ErrorException;

/**
 * 断言工具
 */
public class AssertUtils {
    /**
     * 断言表达式是否为true
     * @param expression 表达式
     * @param message 异常信息
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new ErrorException(message);
        }
    }

    /**
     * 断言表达式是否为false
     * @param expression 表达式
     * @param message 异常信息
     */
    public static void isFalse(boolean expression, String message) {
        isTrue(!expression, message);
    }

    /**
     * 断言对象是null
     * @param object 对象
     * @param message 异常信息
     */
    public static void isNull(Object object, String message) {
        isTrue(object == null, message);
    }

    /**
     * 断言对象不为null
     * @param object 对象
     * @param message 异常信息
     */
    public static void notNull(Object object, String message) {
        isTrue(object != null, message);
    }
}
