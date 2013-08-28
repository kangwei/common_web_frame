/**
 * ClassName: Test
 * CopyRight: OpenSoft
 * Date: 13-7-19
 * Version: 1.0
 */
package com.opensoft.common.event;

import com.opensoft.common.event.annotation.Event;
import com.opensoft.common.event.annotation.OnEvent;
import com.opensoft.common.event.annotation.ParameterType;
import com.opensoft.common.event.annotation.Send;
import com.opensoft.common.event.core.EventThreadPoolMonitor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Description :
 *
 * @author : KangWei
 */
@Component("testBean")
public class Test {

    @Send(events = {
            @Event(eventName = "test", parameterType = ParameterType.ret, fireCondition = "#s.equals('hello world')"),
            @Event(eventName = "test1")

    })
    public String send(String s) {
        System.out.println(s);
//        throw new RuntimeException("我是错误");
        return s.toUpperCase();
    }

    @OnEvent(eventName = "test")
    public void receiver(String s) {
        System.out.println("onEvent :" + s);
    }

    @OnEvent(eventName = "test1")
    public String test1(String s) {
        System.out.println(s);
        return s;
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {
                "classpath:spring/applicationContext-*.xml"
        });
        Test test = (Test) context.getBean("testBean");
        test.send("hello world");
        System.out.println("111111");

        EventThreadPoolMonitor.show();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        EventThreadPoolMonitor.show();
    }
}
