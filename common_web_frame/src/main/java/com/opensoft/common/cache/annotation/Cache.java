package com.opensoft.common.cache.annotation;

import java.lang.annotation.*;

/**
 * Description : Cache
 * 缓存Key=hash(包名+类名)
 * User : 康维
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Cache {
    /**
     * 相关联的缓存类，可以指定多个管理类，每个类就是一个cache
     */
    Class<?>[] relatedCacheClass() default {};

    /**
     * 缓存的键值，支持spel表达式
     */
    String key() default "";

    /**
     * 缓存条件，spel表达式
     */
    String condition() default "";

    /**
     * 生存时间，默认5小时，以秒为单位
     */
    int timeToLive() default 5 * 60 * 60;

    /**
     * 空闲时间，默认5分钟，以秒为单位
     */
    int timeToIdle() default 5 * 60;
}
