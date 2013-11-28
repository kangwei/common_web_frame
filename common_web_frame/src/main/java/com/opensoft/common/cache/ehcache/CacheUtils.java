package com.opensoft.common.cache.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Description: cache工具，封装了ehcache的一些操作，简化使用，
 * 提供缓存的创建，可自定义元素的失效时间和缓存清理的策略
 * ehcache 2.5 版本以上存在overflowToDisk无效的问题，尽管还没到内存容量上限，从第一个put的元素开始，写入内存的同时也写入了磁盘
 * 而且不能持久化，所以选用ehcache 2.1.0的版本（不存在上述问题）
 * User: KangWei
 * Date: 11-6-30
 */
public class CacheUtils {
    private static final Logger log = LoggerFactory.getLogger(CacheUtils.class);

    private static final String CACHE_CONFIG_PATH = "defaultCache.xml";  //默认的缓存配置文件
    public static final String CACHE_DEFAULT_EXPIRE = "default_cache";  //默认的可失效缓存

    private static final int maxElementInMemory = 10000;   //缓存在内存中的最大元素个数
    private static final boolean overflowToDisk = true;   //当达到内存最大个数时是否写入磁盘
    private static final boolean diskPersistent = false;   //是否持久化写入磁盘
    private static final boolean eternal = false;     //是否永久有效，一但设置了，timeout将不起作用。
    private static final long timeToLiveSeconds = 120;  //Element在失效前允许存活时间，秒为单位，0表示永久存在
    private static final long timeToIdleSeconds = 120;  //Element在失效前的允许闲置时间，秒为单位，0表示永久存在
    private static final int maxElementInDisk = 1000000; //磁盘中最大的元素个数
    private static final int diskExpiryThreadIntervalSeconds = 120; //磁盘清理线程执行频率，秒为单位
    private static final int diskSpoolBufferSize = 0;   //磁盘spoolBuffer的大小
//    private static BootstrapCacheLoader bootstrapCacheLoader = new DiskStoreBootstrapCacheLoader(true); //2.5 up(not use in 2.1)

    private static final MemoryStoreEvictionPolicy policy = MemoryStoreEvictionPolicy.LFU;    //清理内存的指定策略，默认较少使用LFU
    private static CacheManager cacheManager; //cache管理类

    /**
     * 先从默认地址读取缓存配置文件，如果没有配置文件，手动创建单例的cacheManager
     *
     * @return cacheManager
     */
    private static CacheManager getCacheManager() {
        return cacheManager;
    }

    public static void start() {
        if (cacheManager == null) {
            URL resource = Thread.currentThread().getContextClassLoader().getResource(CACHE_CONFIG_PATH);
            if (log.isDebugEnabled()) {
                log.debug("加载ehcache默认配置文件，地址：{}", resource != null ? resource.getFile() : null);
            }
            cacheManager = new CacheManager(resource);
            if (log.isDebugEnabled()) {
                log.debug("创建cacheManager成功");
            }
        }
    }

    /**
     * 以默认的参数创建一个指定名称的缓存
     *
     * @param cacheName 缓存名称
     * @return 缓存
     */
    private static Cache createCache(String cacheName) {
        //判断是否创建默认缓存
        if (cacheName == null) {
            cacheName = CACHE_DEFAULT_EXPIRE;
        }

        Cache cache = getCacheManager().getCache(cacheName);
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
        return new CacheConfiguration(cacheName, maxElementInMemory)
                .memoryStoreEvictionPolicy(policy)
                .overflowToDisk(overflowToDisk)
                .diskPersistent(diskPersistent)
//                    .persistence(new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP))    //2.5 up
                .timeToIdleSeconds(timeToIdleSeconds)
                .timeToLiveSeconds(timeToLiveSeconds)
                .eternal(eternal)
                .diskExpiryThreadIntervalSeconds(diskExpiryThreadIntervalSeconds)
                .diskSpoolBufferSizeMB(diskSpoolBufferSize)
                .maxElementsOnDisk(maxElementInDisk);
    }

    /**
     * 获取指定名称的cache对象
     *
     * @param cacheName
     * @return cache对象
     */
    public static Cache getCache(String cacheName) {
        return getCacheManager().getCache(cacheName);
    }

    public static void removeCache(String cacheName) {
        if (log.isDebugEnabled()) {
            log.debug("移除名为{}的缓存", cacheName);
        }
        getCacheManager().removeCache(cacheName);
    }

    /**
     * 取指定名称的缓存中的指定key的值
     *
     * @param cacheName  缓存名称
     * @param elementKey 元素key值
     * @param <T>        泛型（返回类型）
     * @return 缓存的值
     */
    public static <T> T getCacheElement(String cacheName, Object elementKey) {
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

    private static boolean isCacheAlive(Cache cache) {
        return cache != null && cache.getStatus().equals(Status.STATUS_ALIVE);
    }

    /**
     * 取指定名称的缓存中的指定key的值，如果值为空，则自动调取call的返回值，置入缓存，并设置缓存的失效时间
     *
     * @param cacheName          缓存名称
     * @param elementKey         缓存键值
     * @param call               取值函数
     * @param timeToIdleSeconds  空闲时间
     * @param timeTtoLiveSeconds 生存时间
     * @param <T>                泛型（返回类型）
     * @return 缓存的值
     */
    public static <T> T getCacheElement(String cacheName, Object elementKey, Callable call, long timeToIdleSeconds, long timeTtoLiveSeconds) {
        Cache cache = getCache(cacheName);

        if (cache == null) {
            return null;
        }

        if (isCacheAlive(cache)) {
            cache.getCacheConfiguration().setTimeToIdleSeconds(timeToIdleSeconds);
            cache.getCacheConfiguration().setTimeToLiveSeconds(timeTtoLiveSeconds);

            Entry<T> entry = new Entry<T>(elementKey, call, cache);
            T value = entry.getValue();
            if (value != null) {
                return value;
            }
        }

        return null;
    }

    /**
     * 取默认缓存中的指定key的值，如果值为空，则自动调取call的返回值，置入缓存，并设置缓存的失效时间
     *
     * @param elementKey         缓存键值
     * @param call               取值函数
     * @param timeToIdleSeconds  空闲时间
     * @param timeTtoLiveSeconds 生存时间
     * @param <T>                泛型（返回类型）
     * @return 缓存的值
     */
    public static <T> T getCacheElement(Object elementKey, Callable call, long timeToIdleSeconds, long timeTtoLiveSeconds) {
        return getCacheElement(CACHE_DEFAULT_EXPIRE, elementKey, call, timeToIdleSeconds, timeTtoLiveSeconds);
    }

    /**
     * 取指定名称的缓存中的指定key的值，如果值为空，则自动调取call的返回值，置入缓存
     *
     * @param cacheName  缓存名称
     * @param elementKey 缓存键值
     * @param call       取值函数
     * @param <T>        泛型（返回类型）
     * @return 缓存的值
     */
    public static <T> T getCacheElement(String cacheName, Object elementKey, Callable call) {
        return getCacheElement(cacheName, elementKey, call, 0, 0);
    }

    /**
     * 取默认缓存中的指定key的值，如果值为空，则自动调取call的返回值，置入缓存
     *
     * @param elementKey 缓存键值
     * @param call       取值函数
     * @param <T>        泛型（返回类型）
     * @return 缓存的值
     */
    public static <T> T getCacheElement(Object elementKey, Callable call) {
        return getCacheElement(CACHE_DEFAULT_EXPIRE, elementKey, call);
    }

    /**
     * 取默认失效缓存中的指定key的值
     *
     * @param elementKey 缓存键值
     * @param <T>        泛型（返回类型）
     * @return 缓存的值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getCacheElement(Object elementKey) {
        return getCacheElement(CACHE_DEFAULT_EXPIRE, elementKey);
    }

    /**
     * 将元素置入指定缓存
     *
     * @param cacheName  缓存名
     * @param elementKey 元素key
     * @param value      元素值
     */
    public static void putCacheElement(String cacheName, Object elementKey, Object value) {
        Cache cache = createCache(cacheName);

        if (isCacheAlive(cache)) {
            Element element = new Element(elementKey, value);
            cache.put(element);
        }
    }

    /**
     * 置入默认失效缓存
     *
     * @param elementKey 缓存键值
     * @param value      缓存的值
     */
    public static void putCacheElement(Object elementKey, Object value) {
        putCacheElement(CACHE_DEFAULT_EXPIRE, elementKey, value);
    }

    /**
     * 将元素置入指定缓存，并指定失效策略
     *
     * @param cacheName         缓存名
     * @param elementKey        元素key
     * @param value             值
     * @param timeToIdleSeconds 闲置时间
     * @param timeToLiveSeconds 存活时间
     */
    public static void putCacheElement(String cacheName, Object elementKey, Object value,
                                       Integer timeToIdleSeconds, Integer timeToLiveSeconds) {
        Cache cache = createCache(cacheName);

        if (isCacheAlive(cache)) {
            if (log.isDebugEnabled()) {
                log.debug("缓存名:{}缓存键:{}元素:{}置入缓存，ttl={}, tti={}", new Object[]{cacheName, elementKey, value, timeToLiveSeconds, timeToIdleSeconds});
            }
            Element element = new Element(elementKey, value, eternal, timeToIdleSeconds, timeToLiveSeconds);
            cache.put(element);
        }
    }

    /**
     * 将元素置入指定缓存，并指定失效策略
     *
     * @param elementKey        元素key
     * @param value             值
     * @param timeToIdleSeconds 闲置时间
     * @param timeToLiveSeconds 存活时间
     */
    public static void putCacheElement(Object elementKey, Object value,
                                       Integer timeToIdleSeconds, Integer timeToLiveSeconds) {
        putCacheElement(CACHE_DEFAULT_EXPIRE, elementKey, value, timeToIdleSeconds, timeToLiveSeconds);
    }

    public static Object removeFromCache(Object elementKey) {
        return removeFromCache(CACHE_DEFAULT_EXPIRE, elementKey);
    }

    public static Object removeFromCache(String cacheName, Object elementKey) {
        Object objectValue = null;
        Cache cache = getCache(cacheName);

        if (cache == null) {
            return null;
        }

        if (isCacheAlive(cache)) {
            Element element = cache.get(elementKey);
            if (element != null) {
                objectValue = element.getObjectValue();
                cache.remove(elementKey);
                if (log.isDebugEnabled()) {
                    log.debug("移除名为：{}的缓存中缓存键：{}的值", cacheName, elementKey);
                }
            }
        }

        return objectValue;
    }

    public static void removeAllElementFromCache(String cacheName) {
        Cache cache = getCache(cacheName);
        if (isCacheAlive(cache)) {
            cache.removeAll();
        }
    }

    /**
     * 关闭cacheManager
     */
    public static void close() {
        if (cacheManager != null) {
            String[] cacheNames = cacheManager.getCacheNames();
            for (String cacheName : cacheNames) {
                Cache cache = cacheManager.getCache(cacheName);
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
    }

    /**
     * 内部类，定义缓存的取值回调函数
     *
     * @param <T> 泛型（返回类型）
     */
    private static class Entry<T> {
        private final Object elementKey;
        private final Callable call;
        private final Cache cache;
        private T value;

        public Entry(Object elementKey, Callable call, Cache cache) {
            this.elementKey = elementKey;
            this.call = call;
            this.cache = cache;
        }

        /**
         * 回调取值
         *
         * @return 值
         */
        public T getValue() {
            if (isCacheAlive(cache)) {
                Element element = cache.get(elementKey);

                if (element == null) {
                    try {
                        value = (T) call.call();
                        element = new Element(elementKey, value);
                        cache.put(element);
                    } catch (Exception e) {
                        log.warn("名为[" + cache.getName() + "]的缓存置入key为[" + elementKey + "]的值异常", e);
                    }
                } else {
                    value = (T) element.getObjectValue();
                }

                return value;
            }

            return null;
        }
    }
}
