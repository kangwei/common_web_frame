/**
 * ClassName: RedisCacheClientImpl
 * CopyRight: OpenSoft
 * Date: 13-6-13
 * Version: 1.0
 */
package com.opensoft.common.cache.redis;

import com.opensoft.common.cache.CacheClient;
import com.opensoft.common.utils.json.gson.GsonUtils;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Description :
 *
 * @author : KangWei
 */
public class RedisCacheClientImpl implements CacheClient {
    private ShardedJedisPool shardedJedisPool;

    public ShardedJedisPool getShardedJedisPool() {
        return shardedJedisPool;
    }

    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }

    private ShardedJedis getShardedJedis() {
        return shardedJedisPool.getResource();
    }

    @Override
    public void putIntoCache(String cacheName, Object elementKey, Object elementValue) {
        getShardedJedis().set(cacheName + elementKey, GsonUtils.toJson(elementValue));
    }

    @Override
    public void putIntoCache(String cacheName, Object elementKey, Object elementValue, int timeToLive, int timeToIdle) {
        getShardedJedis().set(cacheName + elementKey, GsonUtils.toJson(elementValue));
        getShardedJedis().expire(cacheName + elementKey, timeToLive);
    }

    @Override
    public <T> T getFromCache(String cacheName, Object elementKey) {
        return (T) getShardedJedis().get(cacheName + elementKey);
    }

    @Override
    public Object removeElement(String cacheName, Object elementKey) {
        return getShardedJedis().del(cacheName + elementKey);
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
