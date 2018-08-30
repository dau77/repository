package com.equator.validate;

/**
 * 验证器
 * @param <T>
 */
public interface Validator<T> {

    /**
     * 验证
     * @param value 验证值
     * @return 验证成功返回true，否则返回false
     */
    boolean validate(T value);
}
