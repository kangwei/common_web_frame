/**
 * ClassName: MemcacheClient
 * CopyRight: OpenSoft
 * Date: 13-7-23
 * Version: 1.0
 */
package com.opensoft.common.cache.memcached;

import com.opensoft.common.cache.CacheClient;

/**
 * Description :
 *
 * @author : KangWei
 */
public class MemcacheClient implements CacheClient {
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

    @Override
    public Object removeElement(String cacheName, Object elementKey) {
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
