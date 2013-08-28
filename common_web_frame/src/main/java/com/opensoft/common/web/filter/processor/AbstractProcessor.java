/**
 * ClassName: AbstractProcessor
 * CopyRight: OpenSoft
 * Version: 1.0
 * DATE: 2012-06-11
 */
package com.opensoft.common.web.filter.processor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description: 过滤器基类，所有过滤器都继承这个抽象类，
 * 重写execute方法来实现过滤器功能。
 * 设置跳过过滤器的条件
 *
 * @author : KangWei
 */
public abstract class AbstractProcessor {
    public boolean execute(HttpServletRequest request, HttpServletResponse response) {
        if (skipProcessor()) {
            return true;
        }

        return true;
    }

    protected boolean skipProcessor() {
        return false;
    }

    /**
     * 屏蔽链接地址
     *
     * @param url  访问地址
     * @param urls 地址列表
     * @return url在列表中返回true，否则返回false
     */
    protected boolean shieldUrls(String url, String[] urls) {
        if (urls != null && urls.length > 0) {
            for (String u : urls) {
                if (url.contains(u)) {
                    return true;
                }
            }
        }

        return false;
    }

}
