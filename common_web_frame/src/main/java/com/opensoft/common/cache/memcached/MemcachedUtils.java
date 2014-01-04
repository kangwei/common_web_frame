/**
 * ClassName: MemcachedUtils
 * CopyRight: TalkWeb
 * Date: 13-12-23
 * Version: 1.0
 */
package com.opensoft.common.cache.memcached;

import com.google.code.yanf4j.config.Configuration;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.MemcachedClientCallable;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Description :
 *
 * @author : KangWei
 */
public class MemcachedUtils {
    private static final Logger log = LoggerFactory.getLogger(MemcachedUtils.class);

    private static MemcachedClient client;

    /**
     * 初始化memcache客户端，如果没有指定配置，则加载默认配置
     *
     * @param addressList       memcache服务器地址
     * @param configuration 配置
     * @param failureMode   失效报警，默认为false
     * @param authInfoMap   认证
     * @param poolSize      连接池大小，默认为1
     * @throws IOException 初始化异常
     */
    public static void init(String addressList,
                            Configuration configuration,
                            Boolean failureMode,
                            Map<InetSocketAddress, AuthInfo> authInfoMap,
                            Integer poolSize) throws IOException {
        MemcachedClientBuilder builder = new XMemcachedClientBuilder(addressList);
        if (configuration == null) {
            configuration = new Configuration();
        }
        builder.setConfiguration(configuration);
        if (failureMode == null) {
            failureMode = false;
        }
        builder.setFailureMode(failureMode);
        if (authInfoMap != null) {
            builder.setAuthInfoMap(authInfoMap);
        }
        if (poolSize == null) {
            poolSize = 1;
        }
        builder.setConnectionPoolSize(poolSize);
        log.info("初始化memcache客户端...OK");
        client = builder.build();
    }

    /**
     * 置入缓存
     *
     * @param key        key
     * @param value      value
     * @param expireTime 失效时间，s为单位
     * @throws InterruptedException
     * @throws MemcachedException
     * @throws TimeoutException
     */
    public static void put(String key, Object value, Integer expireTime) throws InterruptedException, MemcachedException, TimeoutException {
        client.set(key, expireTime, value);
    }

    /**
     * 置入缓存，所有在同一命名空间下
     *
     * @param cacheName  缓存命名空间
     * @param key        key
     * @param value      value
     * @param expireTime 失效时间
     * @throws InterruptedException
     * @throws MemcachedException
     * @throws TimeoutException
     */
    public static void put(String cacheName, final String key, final Object value, final Integer expireTime) throws InterruptedException, MemcachedException, TimeoutException {
        client.withNamespace(cacheName, new MemcachedClientCallable<Void>() {
            @Override
            public Void call(MemcachedClient memcachedClient) throws MemcachedException, InterruptedException, TimeoutException {
                memcachedClient.set(key, expireTime, value);
                return null;
            }
        });
    }

    /**
     * 获取缓存值
     *
     * @param key key
     * @param <T> 泛型
     * @return 值
     * @throws InterruptedException
     * @throws MemcachedException
     * @throws TimeoutException
     */
    public static <T> T get(String key) throws InterruptedException, MemcachedException, TimeoutException {
        return client.get(key);
    }

    /**
     * 获取命名空间下的缓存值
     *
     * @param cacheName 命名空间
     * @param key       key
     * @param <T>       泛型
     * @return 值
     * @throws InterruptedException
     * @throws MemcachedException
     * @throws TimeoutException
     */
    public static <T> T get(String cacheName, final String key) throws InterruptedException, MemcachedException, TimeoutException {
        return (T) client.withNamespace(cacheName, new MemcachedClientCallable<Object>() {
            @Override
            public Object call(MemcachedClient memcachedClient) throws MemcachedException, InterruptedException, TimeoutException {
                return memcachedClient.get(key);
            }
        });
    }

    /**
     * 移除缓存
     *
     * @param key key
     * @return 成功/失败
     * @throws InterruptedException
     * @throws MemcachedException
     * @throws TimeoutException
     */
    public static boolean remove(String key) throws InterruptedException, MemcachedException, TimeoutException {
        return client.delete(key);
    }

    /**
     * 移除命名空间下的缓存
     *
     * @param cacheName 命名空间
     * @param key       key
     * @return 成功/失败
     * @throws InterruptedException
     * @throws MemcachedException
     * @throws TimeoutException
     */
    public static boolean remove(final String cacheName, final String key) throws InterruptedException, MemcachedException, TimeoutException {
        return client.withNamespace(cacheName, new MemcachedClientCallable<Boolean>() {
            @Override
            public Boolean call(MemcachedClient client) throws MemcachedException, InterruptedException, TimeoutException {
                return client.delete(key);
            }
        });
    }

    /**
     * 移除命名空间下的所有缓存
     *
     * @param cacheName 命名空间
     * @throws InterruptedException
     * @throws MemcachedException
     * @throws TimeoutException
     */
    public static void removeAll(final String cacheName) throws InterruptedException, MemcachedException, TimeoutException {
        client.invalidateNamespace(cacheName);
    }

    public static void main(String[] args) throws IOException, InterruptedException, MemcachedException, TimeoutException {
        MemcachedUtils.init("127.0.0.1:11211", null, true, null, 1);
        /**单线程测试**/
        /*MemcachedUtils.put("test", "a", 11111, 300);
        MemcachedUtils.put("test", "b", 22222, 300);
        System.out.println("1================>"+MemcachedUtils.get("test", "a"));
        System.out.println("2================>"+MemcachedUtils.get("a"));
        System.out.println("3================>"+MemcachedUtils.get("test", "b"));
        MemcachedUtils.remove("test", "a");
        System.out.println("4================>"+MemcachedUtils.get("test", "a"));
        System.out.println("5================>"+MemcachedUtils.get("test", "b"));

        MemcachedUtils.removeAll("test");
        System.out.println("6================>"+MemcachedUtils.get("test", "a"));
        System.out.println("7================>"+MemcachedUtils.get("test", "b"));*/

        /******************多线程测试*******************************/
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        MemcachedUtils.put(String.valueOf(i), i, 3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (MemcachedException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        MemcachedUtils.put(String.valueOf(i), i + "a", 3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (MemcachedException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        Thread.sleep(500);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        System.out.println(i + "==================>" + MemcachedUtils.get(String.valueOf(i)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (MemcachedException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
