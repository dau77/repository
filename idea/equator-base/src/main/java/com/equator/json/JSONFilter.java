package com.equator.json;

import java.lang.annotation.*;

/**
 * 需要进行过滤时使用。可参考@JsonFilter
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface JSONFilter {

    String value() default "DYNC_FILTER";
}
