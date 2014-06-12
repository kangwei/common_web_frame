/**
 * ClassName: EhCacheClientImpl
 * CopyRight: OpenSoft
 * Date: 12-11-26
 * Version: 1.0
 */
package com.opensoft.common.cache.ehcache;

import com.opensoft.common.cache.AbstractCacheClient;
import com.opensoft.common.cache.CacheClient;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description : ehcacheClient的实现
 * User : 康维
 */
public class EhCacheClientImpl extends AbstractCacheClient {
    private static final Logger log = LoggerFactory.getLogger(EhCacheClientImpl.class);

    private CacheManager cacheManager;

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * @param cacheName    缓存名
     * @param elementKey   缓存key
     * @param elementValue 缓存值
     * @see CacheClient#putIntoCache(String, Object, Object)
     */
    @Override
    public void putIntoCache(String cacheName, Object elementKey, Object elementValue) {
        putIntoCache(cacheName, elementKey, elementValue, 0, 0);
    }

    /**
     * @param cacheName    缓存名
     * @param elementKey   缓存key
     * @param elementValue 缓存值
     * @param timeToLive   失效时间，秒为单位
     * @param timeToIdle   失效时间，秒为单位
     * @see CacheClient#putIntoCache(String, Object, Object, int, int)
     */
    @Override
    public void putIntoCache(String cacheName, Object elementKey, Object elementValue, int timeToLive, int timeToIdle) {
//        CacheUtils.putCacheElement(cacheName, elementKey, elementValue, timeToIdle, timeToLive);
        Cache cache = getCache(cacheName);

        if (isCacheAlive(cache)) {
            if (log.isDebugEnabled()) {
                log.debug("缓存名:{}缓存键:{}元素:{}置入缓存，ttl={}, tti={}", new Object[]{cacheName, elementKey, elementValue, timeToLive, timeToIdle});
            }
            Element element = new Element(elementKey, elementValue, cache.getCacheConfiguration().isEternal(), timeToLive, timeToIdle);
            cache.put(element);
        }
    }

    private Cache getCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        //判断缓存是否已创建
        if (cache == null || !cache.getStatus().equals(Status.STATUS_ALIVE)) {
            //以默认配置创建缓存
            cache = new Cache(getCacheConfiguration(cacheName));
            getCacheManager().addCacheIfAbsent(cache);

            if (log.isDebugEnabled()) {
                log.debug("创建名为{}的cache, cache配置：{}", cacheName, cache.toString());
            }
        }
        return cache;
    }

    private static CacheConfiguration getCacheConfiguration(String cacheName) {
        return new CacheConfiguration(cacheName, 10000)
                .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
                .overflowToDisk(false)
                .diskPersistent(false)
                        //                    .persistence(new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP))    //2.5 up
                .timeToIdleSeconds(120)
                .timeToLiveSeconds(120)
                .eternal(false)
                .diskExpiryThreadIntervalSeconds(120)
                .diskSpoolBufferSizeMB(0)
                .maxElementsOnDisk(0);
    }

    private boolean isCacheAlive(Cache cache) {
        return cache != null && cache.getStatus().equals(Status.STATUS_ALIVE);
    }

    /**
     * @param cacheName  缓存名
     * @param elementKey key
     * @param <T>
     * @return
     * @see CacheClient#getFromCache(String, Object)
     */
    @Override
    public <T> T getFromCache(String cacheName, Object elementKey) {
//        return CacheUtils.getCacheElement(cacheName, elementKey);
        Cache cache = getCache(cacheName);
        if (cache == null) {
            return null;
        }

        if (isCacheAlive(cache)) {
            Element element = cache.get(elementKey);
            if (element != null) {
                if (!element.isExpired()) {
                    T value = (T) element.getObjectValue();

                    if (value != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("取到名为{}的元素：[name={}, value={}]", new Object[]{cacheName, elementKey, value});
                        }
                        return value;
                    }
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("缓存名为[{}]的，对象key为[{}]的缓存对象已失效", cacheName, elementKey);
                    }
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("名为[{}]的元素：[key={}]没有缓存的对象", cacheName, elementKey);
                }
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("没有找到名为[{}]的缓存", cacheName);
            }
        }

        return null;
    }

    /**
     * @param cacheName
     * @param elementKey
     * @return
     * @see CacheClient#removeElement(String, Object)
     */
    @Override
    public <T> T removeElement(String cacheName, Object elementKey) {
//        return CacheUtils.removeFromCache(cacheName, elementKey);
        T objectValue = null;
        Cache cache = getCache(cacheName);

        if (cache == null) {
            return null;
        }

        if (isCacheAlive(cache)) {
            Element element = cache.get(elementKey);
            if (element != null) {
                objectValue = (T) element.getObjectValue();
                cache.remove(elementKey);
                if (log.isDebugEnabled()) {
                    log.debug("移除名为：{}的缓存中缓存键：{}的值", cacheName, elementKey);
                }
            }
        }

        return objectValue;
    }

    /**
     * @param cacheName
     * @see CacheClient#removeCache(String)
     */
    @Override
    public void removeCache(String cacheName) {
//        CacheUtils.removeCache(cacheName);
        cacheManager.removeCache(cacheName);
    }

    @Override
    public void start() {
//        CacheUtils.start();
        log.info("初始化CacheClient...OK");
    }

    @Override
    public void stop() {
//        CacheUtils.close();
        if (cacheManager != null) {
            String[] cacheNames = cacheManager.getCacheNames();
            for (String cacheName : cacheNames) {
                Cache cache = getCache(cacheName);
                boolean persistent = cache.getCacheConfiguration().isDiskPersistent();
                if (persistent) {
                    if (log.isDebugEnabled()) {
                        log.debug("cache名为{}的缓存flush to disk", cacheName);
                    }
                    cache.flush();
                    cache = null;
                }
            }

            cacheManager.shutdown();
            cacheManager = null;
            log.info("关闭cacheManager...OK");
        } else {
            log.warn("cacheManager为空");
        }

        super.stop();
    }
}
