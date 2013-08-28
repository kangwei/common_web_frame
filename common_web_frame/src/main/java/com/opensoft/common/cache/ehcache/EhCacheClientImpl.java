/**
 * ClassName: EhCacheClientImpl
 * CopyRight: OpenSoft
 * Date: 12-11-26
 * Version: 1.0
 */
package com.opensoft.common.cache.ehcache;

import com.opensoft.common.cache.CacheClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description : ehcacheClient的实现
 * User : 康维
 */
public class EhCacheClientImpl implements CacheClient {
    private static final Logger log = LoggerFactory.getLogger(EhCacheClientImpl.class);

    @Override
    public void putIntoCache(String cacheName, Object elementKey, Object elementValue) {
        putIntoCache(cacheName, elementKey, elementValue, 0, 0);
    }

    @Override
    public void putIntoCache(String cacheName, Object elementKey, Object elementValue, int timeToLive, int timeToIdle) {
        CacheUtils.putCacheElement(cacheName, elementKey, elementValue, timeToIdle, timeToLive);
    }

    @Override
    public <T> T getFromCache(String cacheName, Object elementKey) {
        return CacheUtils.getCacheElement(cacheName, elementKey);
    }

    @Override
    public Object removeElement(String cacheName, Object elementKey) {
        return CacheUtils.removeFromCache(cacheName, elementKey);
    }

    @Override
    public void removeCache(String cacheName) {
        CacheUtils.removeCache(cacheName);
    }

    @Override
    public void start() {
        CacheUtils.start();
        log.info("初始化CacheClient...OK");
    }

    @Override
    public void stop() {
        CacheUtils.close();
    }
}
