package com.opensoft.common.web;

import org.springframework.context.ApplicationContext;

import java.io.InputStream;

/**
 * Description : 应用上下文环境接口
 * User : 康维
 */
public interface AppContext {
    /**
     * get resource as input stream
     *
     * @param path path
     * @return stream
     */
    InputStream getResourceAsStream(String path);

    /**
     * get init parameter
     *
     * @param key key
     * @return value
     */
    String getInitParameter(String key);

    /**
     * get attribute
     *
     * @param key key
     * @return value
     */
    Object getAttribute(String key);

    /**
     * set attribute
     *
     * @param key key
     * @param o   value
     */
    void setAttribute(String key, Object o);

    /**
     * remove attribute by key
     *
     * @param key key
     */
    void removeAttribute(String key);

    ApplicationContext getApplicationContext();
}
