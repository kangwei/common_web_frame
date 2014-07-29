/**
 * ClassName: RedisCacheTest
 * CopyRight: TalkWeb
 * Date: 14-1-18
 * Version: 1.0
 */
package com.opensoft.common.cache.ehcache;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Description :
 *
 * @author : KangWei
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"classpath*:spring/applicationContext-aop.xml",
                "classpath*:spring/applicationContext-bean.xml",
                "classpath*:spring/cache-ehcache.xml"})
public class EhcacheCacheTest extends AbstractJUnit4SpringContextTests {


    @Test
    public void set() throws InterruptedException {
        EhCacheClientImpl ehCacheClient = (EhCacheClientImpl) applicationContext.getBean("cacheClient");
        ehCacheClient.start();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "kw");
        map.put("age", 11);
        ehCacheClient.putIntoCache("a", "2", "kw");
        ehCacheClient.putIntoCache("a", "1", "wwwwwww");
        ehCacheClient.putIntoCache("b", "1", "kfdfdfdfw");
        ehCacheClient.putIntoCache("b", "2", "dfjkdjf", 3, 1);
//        Thread.sleep(3400);
        System.out.println(ehCacheClient.getFromCache("b", "2"));
        System.out.println(ehCacheClient.getFromCache("b", "1"));
        ehCacheClient.removeElement("b", "1");
        System.out.println(ehCacheClient.getFromCache("b", "1"));
        ehCacheClient.removeCache("a");
        System.out.println(ehCacheClient.getFromCache("a", "1"));
        Object b = ehCacheClient.lazyLoadFromCache("b", "2", new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return "cccccccc";
            }
        });
        System.out.println("lazy load-------------------------->" + b);
        Object a = ehCacheClient.asynLazyLoadFromCache("a", "1", new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(3000);
                return "dddddddddd";
            }
        });
        System.out.println("async load----------------------->" + a);
        Object a1 = ehCacheClient.asynLazyLoadFromCache("a", "2", new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Thread.sleep(3000);
                return "eeeeeeeeeeeee";
            }
        });
        ehCacheClient.asynLazyLoadFromCache("a", "2", new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return "fffffffff";
            }
        });
        ehCacheClient.asynLazyLoadFromCache("a", "3", new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return "gggggggggg";
            }
        });
        System.out.println("async load other----------------------->" + a1);
        Thread.sleep(1001);
        System.out.println(ehCacheClient.getFromCache("a", "1"));
        System.out.println(ehCacheClient.getFromCache("a", "3"));
        Thread.sleep(3001);
        System.out.println(ehCacheClient.getFromCache("a", "1"));
        System.out.println(ehCacheClient.getFromCache("a", "2"));
//        ehCacheClient.stop();
    }

    @After
    public void after() {

    }
}
