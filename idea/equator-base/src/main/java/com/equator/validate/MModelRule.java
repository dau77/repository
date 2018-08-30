package com.equator.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = { MModelRuleConstranit.class}) //指定约束实现的类
@Target(TYPE_USE)
@Retention(RUNTIME)
@Inherited
public @interface MModelRule {

    String message() default "字段规则不准确"; //可以用代码

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
