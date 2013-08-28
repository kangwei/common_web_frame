/**
 * ClassName: HandlerLoader
 * CopyRight: OpenSoft
 * Date: 12-12-22
 * Version: 1.0
 */
package com.opensoft.common.event.core;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.lang.reflect.Method;

/**
 * Description : 事件加载器
 * User : 康维
 */
public class HandlerLoader {
    private String name;
    private Class clazz;
    private Method method;

    public HandlerLoader() {
    }

    public HandlerLoader(String name, Class clazz, Method method) {
        this.name = name;
        this.clazz = clazz;
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
