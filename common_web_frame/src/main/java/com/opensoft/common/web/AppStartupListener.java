/**
 * ClassName: AppStartupListener
 * CopyRight: OpenSoft
 * Date: 12-12-24
 * Version: 1.0
 */
package com.opensoft.common.web;

import com.opensoft.common.event.core.EventComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Description : Ecp web 方式启动加载
 * User : 康维
 */
public class AppStartupListener implements ServletContextListener {
    private static final Logger log = LoggerFactory.getLogger(AppStartupListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        WebAppContextUtils.init(sce.getServletContext());
        com.opensoft.common.event.core.EventComponent.getInstance().start();
//        CacheClientFactory.getCacheClient(CacheClientFactory.TYPE_EHCACHE).start();
        log.info("[CWF]-组件加载完毕...");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("[CWF]-组件开始停止...");
        com.opensoft.common.event.core.EventComponent.getInstance().stop();
//        CacheClientFactory.getCacheClient(CacheClientFactory.TYPE_EHCACHE).stop();
        log.info("[CWF]-组件停止完毕...");
    }
}
