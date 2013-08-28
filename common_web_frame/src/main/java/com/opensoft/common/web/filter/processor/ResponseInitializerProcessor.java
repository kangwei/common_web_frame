package com.opensoft.common.web.filter.processor;

import com.opensoft.common.web.PageContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 初始化 response 对象
 *
 * @author yiding.he
 */
public class ResponseInitializerProcessor extends AbstractProcessor {

    @Override
    public boolean execute(HttpServletRequest request, HttpServletResponse response) {
        PageContext.setResponse(response);
        return true;
    }
}
