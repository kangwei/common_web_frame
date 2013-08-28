/**
 * ClassName: DaemonThreadFactory
 * CopyRight: OpenSoft
 * Date: 13-1-9
 * Version: 1.0
 */
package com.opensoft.common.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Description :守护线程生成工厂
 *
 * @author : 康维
 */
public class DaemonThreadFactory implements ThreadFactory {
    private static final Logger log = LoggerFactory.getLogger(DaemonThreadFactory.class);

    private static DaemonThreadFactory instance;

    private DaemonThreadFactory() {
    }

    /**
     * @param r Runnable
     * @return Thread
     * @see ThreadFactory#newThread(Runnable)
     */
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = Executors.defaultThreadFactory().newThread(r);
        if (!thread.isDaemon()) {
            if (log.isDebugEnabled()) {
                log.debug("非守护线程{}将重置为守护线程...", thread.getName());
            }
            thread.setDaemon(true);
        }
        return thread;
    }

    /**
     * 获取线程工厂实例
     *
     * @return 工厂实例
     */
    public static DaemonThreadFactory getInstance() {
        if (instance == null) {
            instance = new DaemonThreadFactory();
        }

        return instance;
    }
}
