/**
 * ClassName: EventPublisher
 * CopyRight: OpenSoft
 * Date: 12-12-26
 * Version: 1.0
 */
package com.opensoft.common.event.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description :事件发布者
 * User : 康维
 */
@Deprecated
public class EventPublisher {
    private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);

    /**
     * 在程序中调用，发布事件
     *
     * @param eventName 事件
     * @param args      事件参数
     */
    public static void publishEvent(String eventName, Object... args) {
        if (log.isDebugEnabled()) {
            log.debug("脱离注解，程序调用发布事件" + eventName);
        }
        EventTrigger.fire(eventName, args);
    }
}
