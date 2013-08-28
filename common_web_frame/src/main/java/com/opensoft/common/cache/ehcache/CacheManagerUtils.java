/**
 * ClassName: CacheManagerUtils
 * CopyRight: OpenSoft
 * Date: 12-12-17
 * Version: 1.0
 */
package com.opensoft.common.cache.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Statistics;
import net.sf.ehcache.config.CacheConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Description :Ehcache缓存管理工具
 * User : 康维
 */
public class CacheManagerUtils {
    private static final Logger log = LoggerFactory.getLogger(CacheManagerUtils.class);

    public static int getDiskStoreSize(String cacheName) {
        return getCache(cacheName).getDiskStoreSize();
    }

    private static Cache getCache(String cacheName) {
        return CacheUtils.getCache(cacheName);
    }

    public static long getMemoryStoreSize(String cacheName) {
        return getCache(cacheName).getMemoryStoreSize();
    }

    public static float getAverageGetTime(String cacheName) {
        return getCache(cacheName).getAverageGetTime();
    }

    public static CacheConfiguration getCacheConfiguration(String cacheName) {
        return getCache(cacheName).getCacheConfiguration();
    }

    public static Statistics getStatistics(String cacheName) {
        return getCache(cacheName).getStatistics();
    }

    public static List getKeys(String cacheName) {
        return CacheUtils.getCache(cacheName).getKeys();
    }
}
