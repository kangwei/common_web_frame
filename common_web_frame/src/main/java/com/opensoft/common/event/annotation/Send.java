package com.opensoft.common.event.annotation;

import java.lang.annotation.*;

/**
 * Description : 事件的发送者注解，方法加入该注解，表示发布一个事件/一系列事件
 * User : 康维
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface Send {

    /**
     * 多个事件，注意执行顺序为定义的顺序
     */
    Event[] events();
}
