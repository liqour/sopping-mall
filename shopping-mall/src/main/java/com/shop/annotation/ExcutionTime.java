package com.shop.annotation;

import java.lang.annotation.*;

/**
 * 注解：计算方法的执行时间
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface ExcutionTime {
}
