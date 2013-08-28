/**
 * ClassName: Test
 * CopyRight: OpenSoft
 * Date: 13-7-23
 * Version: 1.0
 */
package com.opensoft.common.cache;

import com.opensoft.common.cache.annotation.Cache;
import com.opensoft.common.cache.annotation.CacheClear;
import com.opensoft.common.cache.annotation.CacheUpdate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Description :
 *
 * @author : KangWei
 */
@Component("testCache")
public class Test {

    @com.opensoft.common.cache.annotation.Cache
    public String get(String s) {
        System.out.println("invoke get" + s);
        return s;
    }

    @com.opensoft.common.cache.annotation.Cache
    public Integer get(Integer i) {
        System.out.println("invoke get" + i);
        return i;
    }

    @com.opensoft.common.cache.annotation.CacheUpdate(key = "'abc'")
    public String update(String i) {
        System.out.println("invoke update" + i);
        return i+100;
    }

    @CacheClear
    public void delete(Integer i) {
        System.out.println("invoke delete");
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                "classpath:spring/applicationContext-*.xml"
        });
        Test test = (Test) context.getBean("testCache");
        test.get("abc");
        test.get(2);
        test.update("aaaa");
        test.delete(2);
        test.get(2);
        test.get("abc");

    }
}
