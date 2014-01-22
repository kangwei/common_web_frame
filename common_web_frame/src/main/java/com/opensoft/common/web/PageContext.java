/**
 * ClassName: PageContext
 * CopyRight: OpenSoft
 * Version: 1.0
 * DATE: 2012-06-11
 */
package com.opensoft.common.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * PageContext 帮助类，将request和response置入context，供程序调用
 *
 * @author KangWei
 */
public class PageContext {
    private static final Logger log = LoggerFactory.getLogger(PageContext.class);

    private static final ThreadLocal<HttpServletRequest> requestTL = new ThreadLocal<HttpServletRequest>();

    private static final ThreadLocal<HttpServletResponse> responseTL = new ThreadLocal<HttpServletResponse>();


    public static void setRequest(HttpServletRequest request) {
        requestTL.set(request);
    }

    public static HttpServletRequest getRequest() {
        return requestTL.get();
    }

    public static void setResponse(HttpServletResponse response) {
        responseTL.set(response);
    }

    public static HttpServletResponse getResponse() {
        return responseTL.get();
    }

    /**
     * 获取相对于当前请求路径的应用根目录。如果当前请求的是一级目录， 则返回 ".."，如果是二级目录，则返回 "../.."，依此类推。
     *
     * @return 相对于当前请求路径的应用根目录
     */
    public static String root() {
        if (getRequest() == null) {
            return "";
        }

        String path = getRequest().getServletPath();

        String root = "./";
        for (int i = 0; i < com.opensoft.common.utils.StringUtils.countMatches(path, "/") - 1; i++) {
            root += "../";
        }

        return com.opensoft.common.utils.StringUtils.removeEnd(root, "/");
    }

    /**
     * 检查页面参数是否为空
     *
     * @param paramName 参数名
     * @return 如果参数值不存在或仅包含空白字符，则返回 true
     */
    public static boolean isEmpty(String paramName) {
        return com.opensoft.common.utils.StringUtils.isBlank(getRequest().getParameter(paramName));
    }

    /**
     * 转发至目标页面
     *
     * @param pageName 目标页面
     * @throws java.io.IOException            如果转发失败
     * @throws javax.servlet.ServletException 如果转发失败
     */
    public static void forwardTo(String pageName) throws IOException,
            ServletException {
        RequestDispatcher dispatcher = getRequest().getRequestDispatcher(pageName);
        dispatcher.forward(getRequest(), getResponse());
    }

    /**
     * 获取参数值
     *
     * @param name 参数名
     * @return 参数值。如果参数不存在则返回空字符串
     */
    public static String getParameter(String name) {
        return getParameter(name, null);
    }

    /**
     * 获取参数值
     *
     * @param name         参数名
     * @param defaultValue 默认值
     * @return 参数值。如果参数不存在则返回给定默认值
     */
    public static String getParameter(String name, String defaultValue) {
        return com.opensoft.common.utils.StringUtils.defaultIfEmpty(getRequest().getParameter(name),
                defaultValue);
    }

    /**
     * 获取整型参数值
     *
     * @param name         参数名
     * @param defaultValue 缺省值
     * @return 参数值。如果参数不存在或者格式不正确则返回缺省值
     */
    public static int getParameterInt(String name, int defaultValue) {
        String value = getRequest().getParameter(name);
        if (value == null || !value.matches("^\\d+$")) {
            return defaultValue;
        } else {
            return Integer.parseInt(value);
        }
    }

    /**
     * 获取用户User_Agent
     *
     * @return 用户User_Agent, 不存在则返回空字符串
     */
    public static String getUserAgent() {
        String user_agent = getRequest().getHeader("user-agent");
        if (user_agent == null) {
            return "";
        }

        return com.opensoft.common.utils.StringUtils.replaceStr(user_agent, "", "$", "\n", " ", "\"", "\r");
    }

    /**
     * 设置 Session 属性
     *
     * @param key   属性名
     * @param value 属性值
     */
    public static void setSessionValue(String key, Object value) {
        getRequest().getSession().setAttribute(key, value);
    }

    /**
     * 获取 Session 属性
     *
     * @param name 属性名
     * @return 属性值
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T getSessionValue(String name) {
        return (T) getRequest().getSession().getAttribute(name);
    }

    /**
     * 从session中取值
     *
     * @param name         session的key值
     * @param defaultValue 默认值
     * @return session中的值，未取到值返回默认值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getSessionValue(String name, T defaultValue) {
        T value = (T) getSessionValue(name);
        return value == null ? defaultValue : value;
    }

    /**
     * 判断服务器是否是本地（即开发环境）
     *
     * @return 如果服务器是本地，则返回 true
     */
    public static boolean isLocalhost() {
        return getRequest().getRequestURL().toString().contains("localhost")
                || getRequest().getRequestURL().toString().contains("127.0.0.1");
    }

    /**
     * 取网站根路径
     *
     * @return 根路径
     */
    public static String getBasePath() {
        if (PageContext.isLocalhost()) {
            return getRequest().getScheme() + "://" + getRequest().getServerName() + ":" + getRequest().getServerPort() + getRequest().getContextPath();
        }
        return getRequest().getScheme() + "://" + getRequest().getServerName() + getRequest().getContextPath();
    }

    /**
     * 是否网络机器人
     *
     * @return 是返回true
     */
    public static boolean isBot() {
        return getUserAgent().contains("bot") || getUserAgent().contains("spider");
    }


    /**
     * 获取客户端ip
     *
     * @return ip
     */
    public static String getClientAddress() {
        String address = getRequest().getHeader("X-Forwarded-For");
        if (address != null && com.opensoft.common.utils.StringUtils.isIpAddress(address)) {
            return address;
        }

        return getRequest().getRemoteAddr();
    }

    /**
     * 取request的attribute
     *
     * @param name key值
     * @param <T>  泛型
     * @return attribute中的值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAttribute(String name) {
        return (T) getRequest().getAttribute(name);
    }

    /**
     * 设置request的attribute值
     *
     * @param key   key
     * @param value value
     */
    public static void setAttribute(String key, Object value) {
        getRequest().setAttribute(key, value);
    }
}

