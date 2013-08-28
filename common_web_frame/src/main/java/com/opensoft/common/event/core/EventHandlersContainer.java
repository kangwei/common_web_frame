/**
 * ClassName: EventHandlersContainer
 * CopyRight: OpenSoft
 * Date: 12-12-22
 * Version: 1.0
 */
package com.opensoft.common.event.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description :
 * User : 康维
 */
public class EventHandlersContainer {
    private static final Logger log = LoggerFactory.getLogger(EventHandlersContainer.class);
    private static Map<String, HandlerLoader> handleMap = new ConcurrentHashMap<String, HandlerLoader>();

    public static void addEventHandler(String eventName, Class clazz, Method method) {
        log.info("[CWF]-注册事件[" + eventName + "]，事件处理类[" + clazz.getName() + "]，事件处理方法[" + method.getName() + "]");
        handleMap.put(eventName, new HandlerLoader(eventName, clazz, method));
    }

    public static HandlerLoader getEventHandler(String eventName) {
        return handleMap.get(eventName);
    }

    public static boolean exist(String eventName) {
        return handleMap.containsKey(eventName);
    }
}
