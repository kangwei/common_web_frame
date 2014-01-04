/**
 * ClassName: MemcacheClient
 * CopyRight: OpenSoft
 * Date: 13-7-23
 * Version: 1.0
 */
package com.opensoft.common.cache.memcached;

import com.opensoft.common.cache.CacheClient;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientCallable;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeoutException;

/**
 * Description :
 *
 * @author : KangWei
 */
public class MemcacheClient implements CacheClient {
    private static final Logger log = LoggerFactory.getLogger(MemcacheClient.class);

    private MemcachedClient memcachedClient;

    private Integer expireTime;

    public Integer getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Integer expireTime) {
        this.expireTime = expireTime;
    }

    public MemcachedClient getMemcachedClient() {
        return memcachedClient;
    }

    public void setMemcachedClient(MemcachedClient memcachedClient) {
        this.memcachedClient = memcachedClient;
    }

    private void putIntoCache(final String cacheName, final Object elementKey, final Object elementValue, final int expireTime) {
        try {
            memcachedClient.withNamespace(cacheName, new MemcachedClientCallable<Void>() {
                @Override
                public Void call(MemcachedClient memcachedClient) throws MemcachedException, InterruptedException, TimeoutException {
                    memcachedClient.set(String.valueOf(elementKey), expireTime, elementValue);
                    return null;
                }
            });
        } catch (Exception e) {
            log.error("put into cache invoke error", e);
        }
    }

    @Override
    public void putIntoCache(String cacheName, Object elementKey, Object elementValue) {
        putIntoCache(cacheName, elementKey, elementValue, expireTime);
    }

    @Override
    public void putIntoCache(String cacheName, Object elementKey, Object elementValue, int timeToLive, int timeToIdle) {
        putIntoCache(cacheName, elementKey, elementValue, timeToLive);
    }

    @Override
    public <T> T getFromCache(String cacheName, final Object elementKey) {
        try {
            return (T) memcachedClient.withNamespace(cacheName, new MemcachedClientCallable<Object>() {
                @Override
                public Object call(MemcachedClient memcachedClient) throws MemcachedException, InterruptedException, TimeoutException {
                    return memcachedClient.get(String.valueOf(elementKey));
                }
            });
        } catch (Exception e) {
            log.error("get cache key={} invoke error ", e);
        }

        return null;
    }

    @Override
    public Object removeElement(String cacheName, final Object elementKey) {
        try {
            return memcachedClient.withNamespace(cacheName, new MemcachedClientCallable<Boolean>() {
                @Override
                public Boolean call(MemcachedClient client) throws MemcachedException, InterruptedException, TimeoutException {
                    return client.delete(String.valueOf(elementKey));
                }
            });
        } catch (Exception e) {
            log.error("remove cache key={} invoke error ", e);
        }

        return false;
    }

    @Override
    public void removeCache(String cacheName) {
        try {
            memcachedClient.invalidateNamespace(cacheName);
        } catch (Exception e) {
            log.error("removeCache invoke error ", e);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        try {
            memcachedClient.shutdown();
        } catch (Exception e) {
            log.error("stop memcache invoke error ", e);
        }
    }
}
