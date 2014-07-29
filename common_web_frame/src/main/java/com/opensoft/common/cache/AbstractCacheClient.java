/**
 * ClassName: AbstractCacheClient
 * CopyRight: TalkWeb
 * Date: 2014/6/12
 * Version: 1.0
 */
package com.opensoft.common.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * Description : 所有CacheClient的抽象实现，主要提供延迟加载和异步延迟加载的统一实现
 *
 * @author : KangWei
 */
public class AbstractCacheClient implements CacheClient {
    private static final Logger log = LoggerFactory.getLogger(AbstractCacheClient.class);



    @Override
    public void putIntoCache(String cacheName, Object elementKey, Object elementValue) {

    }

    @Override
    public void putIntoCache(String cacheName, Object elementKey, Object elementValue, int timeToLive, int timeToIdle) {

    }

    @Override
    public <T> T getFromCache(String cacheName, Object elementKey) {
        return null;
    }

    /**
     * @see com.opensoft.common.cache.CacheClient#lazyLoadFromCache(String, Object, java.util.concurrent.Callable)
     * @param cacheName  缓存名
     * @param elementKey key
     * @param loadSource 加载源
     * @param <T>
     * @return
     */
    @Override
    public <T> T lazyLoadFromCache(String cacheName, Object elementKey, Callable<T> loadSource) {
        T t = getFromCache(cacheName, elementKey);
        if (t == null) {
            try {
                t = loadSource.call();
            } catch (Exception e) {
                log.error("lazy load from cache invoke error", e);
            }
            putIntoCache(cacheName, elementKey, t);
        }

        return t;
    }

    /**
     * @see com.opensoft.common.cache.CacheClient#asynLazyLoadFromCache(String, Object, java.util.concurrent.Callable)
     * @param cacheName  缓存名
     * @param elementKey key
     * @param loadSource 加载源
     * @param <T>
     * @return
     */
    @Override
    public <T> T asynLazyLoadFromCache(String cacheName, Object elementKey, Callable<T> loadSource) {
        T t = getFromCache(cacheName, elementKey);

        new RefreshTask(this, cacheName, elementKey, loadSource).start();

        return t;
    }

    @Override
    public <T> T removeElement(String cacheName, Object elementKey) {
        return null;
    }

    @Override
    public void removeCache(String cacheName) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
