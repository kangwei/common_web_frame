/**
 * ClassName: RedisCacheTest
 * CopyRight: TalkWeb
 * Date: 14-1-18
 * Version: 1.0
 */
package com.opensoft.common.cache.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Description :
 *
 * @author : KangWei
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"classpath*:spring/applicationContext-aop.xml",
                "classpath*:spring/applicationContext-bean.xml",
                "classpath*:spring/cache-redis.xml"})
public class RedisCacheTest extends AbstractJUnit4SpringContextTests {


    @Test
    public void set() throws InterruptedException {
        RedisCacheClientImpl redisCacheClient = (RedisCacheClientImpl) applicationContext.getBean("redisCacheClient");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "kw");
        map.put("age", 11);
        redisCacheClient.putIntoCache("a", "2", "kw");
        redisCacheClient.putIntoCache("a", "1", "wwwwwww");
        redisCacheClient.putIntoCache("b", "1","kfdfdfdfw");
        redisCacheClient.putIntoCache("b", "2", "dfjkdjf", 3, 1);
        Thread.sleep(3400);
        System.out.println(redisCacheClient.getFromCache("b", "2"));
        System.out.println(redisCacheClient.getFromCache("b", "1"));
        redisCacheClient.removeElement("b", "1");
        System.out.println(redisCacheClient.getFromCache("b", "1"));
        redisCacheClient.removeCache("a");
        System.out.println(redisCacheClient.getFromCache("a", "1"));
    }
}
