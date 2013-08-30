package com.opensoft.common.event.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description : 事件的发送者注解，方法加入该注解，表示发布一个事件/一系列事件
 * User : 康维
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Send {

    /**
     * 多个事件，注意执行顺序为定义的顺序
     */
    Event[] events();
}
