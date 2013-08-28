/**
 * ClassName: ThreadPoolManager
 * CopyRight: OpenSoft
 * Date: 13-6-9
 * Version: 1.0
 */
package com.opensoft.common.pool;

import com.opensoft.common.utils.CloseableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description : 线程池管理工具
 * 管理框架的所有线程池
 *
 * @author : KangWei
 */
@Deprecated
public class ThreadPoolManager {
    private static final Logger log = LoggerFactory.getLogger(ThreadPoolManager.class);
    public static final String EVENT_POOL = "event_pool";
    public static final String PARALLEL_POOL = "parallel_pool";

    /**
     * 初始化并行处理线程池
     */
    public static void startParallelPool() {
        ExecutorService parallel_pool = Executors.newCachedThreadPool(DaemonThreadFactory.getInstance());
        com.opensoft.common.pool.ThreadPool.put(PARALLEL_POOL, parallel_pool);
        log.info("[CWF]-初始化[{}]线程池{}...OK", PARALLEL_POOL, parallel_pool);
    }

    /**
     * 关闭并行处理线程池
     */
    public static void stopParallelPool() {
        ExecutorService parallel_pool = com.opensoft.common.pool.ThreadPool.get(PARALLEL_POOL);
        CloseableUtils.shutdown(parallel_pool);
    }

    public static void startEventThreadPool() {
        ExecutorService event_pool = Executors.newCachedThreadPool(DaemonThreadFactory.getInstance());
        com.opensoft.common.pool.ThreadPool.put(EVENT_POOL, event_pool);
        log.info("[CWF]-初始化[{}]线程池{}...OK", EVENT_POOL, event_pool);
    }

    public static void stopEventThreadPool() {
        ExecutorService event_pool = com.opensoft.common.pool.ThreadPool.get(EVENT_POOL);
        CloseableUtils.shutdown(event_pool);
    }

    /**
     * 取得事件线程池
     *
     * @return event_pool
     */
    public static ExecutorService getEventThreadPool() {
        ExecutorService executorService = com.opensoft.common.pool.ThreadPool.get(EVENT_POOL);
        Assert.notNull(executorService, "事件线程池未初始化");
        return executorService;
    }

    /**
     * 取得并行处理线程池
     *
     * @return parallel_pool
     */
    public static ExecutorService getParallelThreadPool() {
        ExecutorService executorService = com.opensoft.common.pool.ThreadPool.get(PARALLEL_POOL);
        Assert.notNull(executorService, "并行处理线程池未初始化");
        return executorService;
    }
}
