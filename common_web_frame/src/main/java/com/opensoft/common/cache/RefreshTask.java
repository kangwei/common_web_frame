/**
 * ClassName: RefreshTask
 * CopyRight: TalkWeb
 * Date: 2014/6/16
 * Version: 1.0
 */
package com.opensoft.common.cache;

import com.opensoft.common.pool.DaemonThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Description : 异步加载缓存线程
 *
 * @author : KangWei
 */
public class RefreshTask extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RefreshTask.class);

    //异步延迟加载的线程池
    private ExecutorService executorService = new ThreadPoolExecutor(1, 10, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1000), DaemonThreadFactory.getInstance());

    private CacheClient cacheClient;

    private String cacheName;

    private Object cacheKey;

    private Callable callable;

    /**
     * 正在加载的key
     */
    private static Set<Object> asyncLoadKeys = new HashSet<Object>();

    public RefreshTask(CacheClient cacheClient, String cacheName, Object cacheKey, Callable callable) {
        this.cacheClient = cacheClient;
        this.cacheName = cacheName;
        this.cacheKey = cacheKey;
        this.callable = callable;
    }

    @Override
    public void run() {
        if (asyncLoadKeys.contains(cacheKey)) {
            log.info("key named {} is already in async load thread running...", cacheKey);
        } else {
            asyncLoadKeys.add(cacheKey);

            Future future = executorService.submit(callable);
            try {
                cacheClient.putIntoCache(cacheName, cacheKey, future.get());
            } catch (InterruptedException e) {
                log.error("asyn load InterruptedException", e);
            } catch (ExecutionException e) {
                log.error("asyn load ExecutionException", e);
            }
            synchronized (asyncLoadKeys) {
                asyncLoadKeys.remove(cacheKey);
            }
        }

    }
}
