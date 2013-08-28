/**
 * ClassName: WebAppContextUtils
 * CopyRight: OpenSoft
 * Date: 12-12-25
 * Version: 1.0
 */
package com.opensoft.common.web;

import com.opensoft.common.exception.AppRuntimeException;

import javax.servlet.ServletContext;

/**
 * Description : Web环境上下文工具类，用于获取WebAppContext
 * User : 康维
 */
public class WebAppContextUtils {
    private static WebAppContext webAppContext;

    /**
     * 获取WebAppContext，单例模式
     *
     * @return WebAppContext
     */
    public static WebAppContext getWebAppContext() {
        if (webAppContext == null) {
            throw new com.opensoft.common.exception.AppRuntimeException("未初始化");
        }

        return webAppContext;
    }

    public static void init(ServletContext servletContext) {
        webAppContext = new WebAppServletContext(servletContext);
    }
}
