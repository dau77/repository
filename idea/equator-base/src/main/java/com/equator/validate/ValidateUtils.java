package com.equator.validate;

import com.equator.exception.ErrorException;
import com.equator.context.SpringContext;

import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class ValidateUtils {
    /**
     * 验证
     * @param object
     * @param group
     * @param <T>
     * @return
     */
    public static <T> void validate(T object, Class group) {
        ValidateGroupHolder.set(new Class[]{group});
        ValidatorFactory factory = SpringContext.getBean(ValidatorFactory.class);
        javax.validation.Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations;
        try {
            violations = validator.validate(object);
        } finally {
            ValidateGroupHolder.remove();
        }
        if (!violations.isEmpty()) {
            throw new ErrorException(violations.iterator().next().getMessage());
        }
    }
}
