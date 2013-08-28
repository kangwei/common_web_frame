package com.opensoft.common.cache;


import com.opensoft.common.StartAble;

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
     * @param timeToLive     失效时间，秒为单位
     * @param timeToIdle     失效时间，秒为单位
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
     * @param cacheName
     * @param elementKey
     * @return
     */
    public Object removeElement(String cacheName, Object elementKey);

    /**
     * @param cacheName
     */
    public void removeCache(String cacheName);
}
