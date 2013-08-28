package com.opensoft.common.web;

import javax.servlet.ServletContext;

/**
 * Description :
 * User : 康维
 */
public interface WebAppContext extends AppContext {
    public ServletContext getServletContext();
}
