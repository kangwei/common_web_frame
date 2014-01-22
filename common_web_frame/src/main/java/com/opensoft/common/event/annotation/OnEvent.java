package com.opensoft.common.event.annotation;

import java.lang.annotation.*;

/**
 * Description :  事件的接收者，接收指定的事件进行处理
 * User : 康维
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface OnEvent {
    /**
     * 事件名
     */
    String eventName();
}
