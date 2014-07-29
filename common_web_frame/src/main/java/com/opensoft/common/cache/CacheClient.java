package com.opensoft.common.cache;


import java.util.concurrent.Callable;

/**
 * Description : cache的client
 * User : 康维
 */
public interface CacheClient extends com.opensoft.common.StartAble {
    /**
     * 置入缓存（默认不失效）
     *
     * @param cacheName    缓存名
     * @param elementKey   缓存key
     * @param elementValue 缓存值
     */
    public void putIntoCache(String cacheName, Object elementKey, Object elementValue);

    /**
     * 置入缓存，带失效时间
     *
     * @param cacheName    缓存名
     * @param elementKey   缓存key
     * @param elementValue 缓存值
     * @param timeToLive   失效时间，秒为单位
     * @param timeToIdle   失效时间，秒为单位
     */
    public void putIntoCache(String cacheName, Object elementKey, Object elementValue, int timeToLive, int timeToIdle);

    /**
     * 从缓存中取值
     *
     * @param cacheName  缓存名
     * @param elementKey key
     * @param <T>        值的泛型
     * @return 值
     */
    public <T> T getFromCache(String cacheName, Object elementKey);

    /**
     * 延迟加载缓存，如果缓存中没有值，会调用获取Callable取值置入缓存
     *
     * @param cacheName  缓存名
     * @param elementKey key
     * @param loadSource 加载源
     * @param <T>        值的泛型
     * @return 值
     */
    public <T> T lazyLoadFromCache(String cacheName, Object elementKey, Callable<T> loadSource);

    /**
     * 异步延迟加载，缓存有值，直接返回，无值，返回null，后台单独线程调用取值置入缓存
     *
     * @param cacheName  缓存名
     * @param elementKey key
     * @param loadSource 加载源
     * @param <T>        值的泛型
     * @return 值
     */
    public <T> T asynLazyLoadFromCache(String cacheName, Object elementKey, Callable<T> loadSource);

    /**
     * 删除元素
     *
     * @param cacheName  缓存名
     * @param elementKey key
     * @return 值
     */
    public <T> T removeElement(String cacheName, Object elementKey);

    /**
     * 移除缓存所有值
     *
     * @param cacheName 缓存名
     */
    public void removeCache(String cacheName);
}
