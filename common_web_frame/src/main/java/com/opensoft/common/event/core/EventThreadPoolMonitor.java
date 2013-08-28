/**
 * ClassName: EventThreadPoolMonitor
 * CopyRight: OpenSoft
 * Date: 13-7-23
 * Version: 1.0
 */
package com.opensoft.common.event.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Description :
 *
 * @author : KangWei
 */
public class EventThreadPoolMonitor {
    private static final Logger log = LoggerFactory.getLogger(EventThreadPoolMonitor.class);

    private static ThreadPoolTaskExecutor executor;

    public static void show() {
        StringBuilder strBuff = new StringBuilder();
        strBuff.append("CurrentPoolSize : ").append(executor.getPoolSize()).append("\n");
        strBuff.append(" - CorePoolSize : ").append(executor.getCorePoolSize()).append("\n");
        strBuff.append(" - MaximumPoolSize : ").append(executor.getMaxPoolSize()).append("\n");
        strBuff.append(" - ActiveTaskCount : ").append(executor.getActiveCount()).append("\n");
        strBuff.append(" - KeepAliveSeconds : ").append(executor.getKeepAliveSeconds()).append("\n");
        strBuff.append(" - QueueSize : ").append(executor.getThreadPoolExecutor().getQueue().size()).append("\n");
        strBuff.append(" - CompletedTaskCount : ").append(executor.getThreadPoolExecutor().getCompletedTaskCount()).append("\n");
        strBuff.append(" - TotalTaskCount : ").append(executor.getThreadPoolExecutor().getTaskCount()).append("\n");
        strBuff.append(" - isTerminated : ").append(executor.getThreadPoolExecutor().isTerminated());

        log.info(strBuff.toString());
    }

    static ThreadPoolTaskExecutor getExecutor() {
        return executor;
    }

    static void setExecutor(ThreadPoolTaskExecutor executor) {
        EventThreadPoolMonitor.executor = executor;
    }
}
