/**
 * ClassName: RedisCacheClientImpl
 * CopyRight: OpenSoft
 * Date: 13-6-13
 * Version: 1.0
 */
package com.opensoft.common.cache.redis;

import com.opensoft.common.cache.AbstractCacheClient;
import com.opensoft.common.utils.SerializeUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Collection;
import java.util.Set;

/**
 * Description :
 *
 * @author : KangWei
 */
public class RedisCacheClientImpl extends AbstractCacheClient {
    private ShardedJedisPool shardedJedisPool;

    private ShardedJedisPoolFactory factory;

    public ShardedJedisPoolFactory getFactory() {
        return factory;
    }

    public void setFactory(ShardedJedisPoolFactory factory) {
        this.factory = factory;
    }

    public ShardedJedisPool getShardedJedisPool() {
        return shardedJedisPool;
    }

    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }

    private ShardedJedis getShardedJedis() {
        if (shardedJedisPool == null) {
            shardedJedisPool = factory.getShardedJedisPool();
        }
        return shardedJedisPool.getResource();
    }

    @Override
    public void putIntoCache(String cacheName, Object elementKey, Object elementValue) {
        getShardedJedis().set(SerializeUtil.serialize(cacheName + elementKey), SerializeUtil.serialize(elementValue));
    }

    @Override
    public void putIntoCache(String cacheName, Object elementKey, Object elementValue, int timeToLive, int timeToIdle) {
        putIntoCache(cacheName, elementKey, elementValue);
        getShardedJedis().expire(SerializeUtil.serialize(cacheName + elementKey), timeToLive);
    }

    @Override
    public <T> T getFromCache(String cacheName, Object elementKey) {
        byte[] bytes = getShardedJedis().get(SerializeUtil.serialize(cacheName + elementKey));
        if (bytes != null) {
            return (T) SerializeUtil.unSerialize(bytes);
        }

        return null;
    }

    @Override
    public <T> T removeElement(String cacheName, Object elementKey) {
        Collection<Jedis> allShards = getShardedJedis().getAllShards();
        for (Jedis jedis : allShards) {
            jedis.del(SerializeUtil.serialize(cacheName + elementKey));
        }

        return null;
    }

    @Override
    public void removeCache(String cacheName) {
        ShardedJedis shardedJedis = getShardedJedis();
        Collection<Jedis> allShards = shardedJedis.getAllShards();
        for (Jedis jedis : allShards) {
            Set<byte[]> keys = jedis.keys(SerializeUtil.serialize(cacheName + "*"));
            for (byte[] key : keys) {
                jedis.del(key);
            }
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        getShardedJedis().disconnect();
        super.stop();
    }
}
