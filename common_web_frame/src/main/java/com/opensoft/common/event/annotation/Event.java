package com.opensoft.common.event.annotation;

import java.lang.annotation.*;

/**
 * Description : 单个事件注解
 *
 * @author : KangWei
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface Event {
    /**
     * 事件名
     */
    String eventName();

    /**
     * 事件参数类型，有参数和返回值两种
     */
    ParameterType parameterType() default ParameterType.arg;

    /**
     * 触发条件，支持spel表达式
     */
    String fireCondition() default "";
}
