package com.opensoft.common.web;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.Map;

/**
 * 访问 ServletContext 的类
 *
 * @author yiding.he
 */
public class ApplicationServletContext {

    private static ServletContext context;

    public static void setContext(ServletContext context) {
        ApplicationServletContext.context = context;
    }

    /**
     * 设置 ServletContext 属性
     *
     * @param key   属性名
     * @param value 属性值
     */
    public static void setAttribute(String key, String value) {
        context.setAttribute(key, value);
    }
    
    public static void removeAttribute(String key){
    	context.removeAttribute(key);
    }
    
    
    /**
     * 设置 ServletContext 属性
     *
     * @param key   属性名
     * @param value 属性值
     */
    public static void setAttribute(String key, List<String> value) {
        context.setAttribute(key, value);
    }
    
    /**
     * 设置 ServletContext 属性
     *
     * @param key   属性名
     * @param value 属性值
     */
    public static void setAttribute(String key, Map<String,String> value) {
        context.setAttribute(key, value);
    }
    
    public static Object getAttribute(String key) {
	return context.getAttribute(key);
    }

    /**
     * 获取绝对路径
     *
     * @param path 应用内路径
     *
     * @return 绝对路径
     */
    public static String getRealPath(String path) {
        return context.getRealPath(path);
    }
}
