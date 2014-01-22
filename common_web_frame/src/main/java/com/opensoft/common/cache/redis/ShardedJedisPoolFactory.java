/**
 * ClassName: ShardedJedisPoolFactory
 * CopyRight: TalkWeb
 * Date: 14-1-18
 * Version: 1.0
 */
package com.opensoft.common.cache.redis;

import com.opensoft.common.utils.StringUtils;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 *
 * @author : KangWei
 */
public class ShardedJedisPoolFactory {
    private ShardedJedisPool shardedJedisPool;

    private String servers;

    private JedisPoolConfig config;

    public ShardedJedisPool getShardedJedisPool() {
        if (shardedJedisPool == null) {
            shardedJedisPool = new ShardedJedisPool(config, toServers(servers));
        }
        return shardedJedisPool;
    }

    private List<JedisShardInfo> toServers(String servers) {
        if (StringUtils.isEmpty(servers)) {
            return null;
        }
        List<JedisShardInfo> shardInfos = new ArrayList<JedisShardInfo>();
        String[] serverArray = servers.split(",", -1);
        for (String s : serverArray) {
            String[] server = s.split(":", -1);
            String ip = server[0];
            int port = Integer.parseInt(server[1]);
            JedisShardInfo shardInfo = new JedisShardInfo(ip, port);
            shardInfos.add(shardInfo);
        }

        return shardInfos;

    }

    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public JedisPoolConfig getConfig() {
        return config;
    }

    public void setConfig(JedisPoolConfig config) {
        this.config = config;
    }
}
