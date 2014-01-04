package com.opensoft.common.cache.annotation;

import java.lang.annotation.*;

/**
 * Description : 移除缓存注解
 *
 * @author : KangWei
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheClear {
    /**
     * cacheName
     */
    String cacheName() default "";

    /**
     * 缓存的键值，支持spel表达式
     */
    String key() default "";

    /**
     * 缓存条件，spel表达式
     */
    String condition() default "";

    /**
     * 清除所有
     */
    boolean allClear() default false;
}
