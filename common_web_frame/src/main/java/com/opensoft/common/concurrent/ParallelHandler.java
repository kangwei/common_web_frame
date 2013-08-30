/**
 * ClassName: ParallelHandler
 * CopyRight: OpenSoft
 * Date: 13-6-13
 * Version: 1.0
 */
package com.opensoft.common.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Description : 并行计算辅助
 *
 * @author : KangWei
 */
public class ParallelHandler {
    private static final Logger log = LoggerFactory.getLogger(ParallelHandler.class);

    private ParallelHandler handler;

    private ExecutorService pool;

    private ParallelHandler() {
        com.opensoft.common.pool.ThreadPoolManager.startParallelPool();
        pool = com.opensoft.common.pool.ThreadPoolManager.getParallelThreadPool();
    }

    public ParallelHandler getHandler() {
        if (handler == null) {
            handler = new ParallelHandler();
        }

        return handler;
    }

    public void submit(Runnable runnable) {
        pool.submit(runnable);
    }

    public <V> Future<V> submit(Callable<V> callable) {
        return pool.submit(callable);
    }
}
