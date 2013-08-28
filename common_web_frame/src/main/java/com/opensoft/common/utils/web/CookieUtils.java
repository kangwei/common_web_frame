package com.opensoft.common.utils.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description: Cookie工具类，提供操作Cookie的一些方法
 * <p/>
 * LoginUser: KangWei
 * Date: 11-9-28
 */
public class CookieUtils {

    public static final String NULL = "";   //空

    /**
     * cookie中存在key的值
     *
     * @param request request
     * @param key     key
     * @return 存在返回true，否则返回false
     */
    public static boolean exists(HttpServletRequest request, String key) {
        if (request.getCookies() == null) {
            return false;
        }

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(key)
                    && com.opensoft.common.utils.StringUtils.isNotEmpty(cookie.getValue())) {
                return true;
            }
        }

        return false;
    }

    /**
     * 取cookie中的指定key的值
     *
     * @param request request
     * @param key     key
     * @return 值
     */
    public static String getCookieValue(HttpServletRequest request, String key) {
        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }

        return NULL;
    }

    /**
     * 设置cookie中的值
     *
     * @param response      response
     * @param key           key
     * @param value         value
     * @param maxLiveSecond maxLiveSecond 秒为单位
     */
    public static void writeCookie(HttpServletResponse response, String key, String value, int maxLiveSecond) {
        writeCookie(response, key, value, "/", maxLiveSecond);
    }

    /**
     * 设置cookie中的值
     *
     * @param response      response
     * @param key           key
     * @param value         value
     * @param domain        domain
     * @param maxLiveSecond maxLiveSecond 秒为单位
     */
    public static void writeCookie(HttpServletResponse response, String key, String value, String domain, int maxLiveSecond) {

        Cookie cookie = new Cookie(key, value);
        cookie.setPath(domain);

        //最长存活时间30天
        cookie.setMaxAge(maxLiveSecond);
        response.addCookie(cookie);
    }

    /**
     * 删除指定key的cookie值
     *
     * @param response response
     * @param key      key
     */
    public static void removeCookieValue(HttpServletResponse response, String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
