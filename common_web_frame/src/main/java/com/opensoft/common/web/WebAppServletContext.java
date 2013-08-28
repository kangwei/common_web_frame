/**
 * ClassName: WebAppServletContext
 * CopyRight: OpenSoft
 * Date: 12-12-24
 * Version: 1.0
 */
package com.opensoft.common.web;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import java.io.InputStream;

/**
 * Description : servlet上下文环境
 * User : 康维
 */
public class WebAppServletContext implements WebAppContext {
    private ServletContext servletContext;

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public WebAppServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * @see
     */
    @Override
    public InputStream getResourceAsStream(String name) {
        return servletContext.getResourceAsStream(name);
    }

    @Override
    public String getInitParameter(String key) {
        return servletContext.getInitParameter(key);
    }

    @Override
    public Object getAttribute(String key) {
        return servletContext.getAttribute(key);
    }

    @Override
    public void setAttribute(String key, Object o) {
        servletContext.setAttribute(key, o);
    }

    @Override
    public void removeAttribute(String key) {
        servletContext.removeAttribute(key);
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return WebApplicationContextUtils.getWebApplicationContext(servletContext);
    }
}
