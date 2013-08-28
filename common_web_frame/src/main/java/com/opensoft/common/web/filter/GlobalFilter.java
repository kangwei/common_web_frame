package com.opensoft.common.web.filter;

import com.opensoft.common.web.filter.processor.AbstractProcessor;
import com.opensoft.common.utils.CollectionUtils;
import com.opensoft.common.utils.StringUtils;
import com.opensoft.common.web.filter.processor.AbstractProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @author KangWei
 * @description 全局过滤器
 */
public class GlobalFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(GlobalFilter.class);

    // 注册处理类实例。新的逻辑写到新的 AbstractProcessor 类里面。注意加载过滤器的顺序
    private List<AbstractProcessor> processors;

    private String skipSuffixNamePattern;

    /////////////////////////////////////////

    public List<AbstractProcessor> getProcessors() {
        return processors;
    }

    public void setProcessors(List<AbstractProcessor> processors) {
        this.processors = processors;
    }

    public String getSkipSuffixNamePattern() {
        return skipSuffixNamePattern;
    }

    public void setSkipSuffixNamePattern(String skipSuffixNamePattern) {
        this.skipSuffixNamePattern = skipSuffixNamePattern;
    }

    /**
     * 应用初始化
     *
     * @param filterConfig 启动配置
     * @throws javax.servlet.ServletException 异常
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        // 应用初始化在 ApplicationListener 类中实现
    }

    // 不要改这个方法。新的逻辑写到新的 Processor 类里面。
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        request.setCharacterEncoding("UTF-8");
        try {
            if (!isSkip(request.getRequestURL().toString())) {
                log.info("请求 => " + parseRequest(request));

                if (com.opensoft.common.utils.CollectionUtils.isNotEmpty(processors)) {
                    for (AbstractProcessor processor : processors) {
                        if (!processor.execute(request, response)) {
                            return;
                        }
                    }
                }
            }
            chain.doFilter(servletRequest, servletResponse);
        } catch (ServletException e) {
            log.error("过滤器有异常发生:", e);
            throw e;
        }

    }

    /**
     * 输出所有请求和请求的参数
     *
     * @param request 请求
     * @return 请求输出
     */
    private static String parseRequest(HttpServletRequest request) {
        Map map = request.getParameterMap();
        String result = request.getRequestURL().toString();
        for (Object name : map.keySet()) {
            result += "\n  " + name + "=" + Arrays.toString((String[]) map.get(name));
        }
        return result;
    }

    public void destroy() {
        // 应用结束处理在 ApplicationListener 类中实现
    }

    /**
     * 打印session信息
     *
     * @param request request
     */
    private static void parseSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String result = "";

        Enumeration names = session.getAttributeNames();
        while (names.hasMoreElements()) {
            String key = (String) names.nextElement();
            result += "\n  " + key + "=" + session.getAttribute(key);
        }

        log.info("session：" + result);
    }

    /**
     * 是否静态资源
     *
     * @param url url地址
     * @return true/false
     */
    private boolean isSkip(String url) {
//        return url.matches(".*(css|jpg|png|gif|js)");
        return com.opensoft.common.utils.StringUtils.isNotEmpty(skipSuffixNamePattern) && url.matches(skipSuffixNamePattern);

    }
}