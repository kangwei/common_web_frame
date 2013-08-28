/**
 * ClassName: WebApplicationContextWrapUtils
 * CopyRight: OpenSoft
 * Date: 13-2-22
 * Version: 1.0
 */
package com.opensoft.common.utils.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

/**
 * Description : web 应用Spring bean工具类
 *
 * @author : 康维
 */
public class WebApplicationContextWrapUtils extends WebApplicationContextUtils {
    private static final Logger log = LoggerFactory.getLogger(WebApplicationContextWrapUtils.class);

    /**
     * 根据类获取bean
     *
     * @param clazz          类
     * @param servletContext servlet上下文
     * @return bean
     */
    public static Object getBean(Class<?> clazz, ServletContext servletContext) {
        return getWebApplicationContext(servletContext).getBean(clazz);
    }

    /**
     * 根据名称获取bean
     *
     * @param beanName       beanName
     * @param servletContext servlet上下文
     * @return bean
     */
    public static Object getBean(String beanName, ServletContext servletContext) {
        return getWebApplicationContext(servletContext).getBean(beanName);
    }

    /**
     * 根据bean的名称和参数获取bean
     *
     * @param beanName beanName
     * @param sc       servlet上下文
     * @param args     参数
     * @return bean
     */
    public static Object getBean(String beanName, ServletContext sc, Object... args) {
        return getWebApplicationContext(sc).getBean(beanName, args);
    }
}
