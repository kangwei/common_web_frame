/**
 * ClassName: EventTrigger
 * CopyRight: OpenSoft
 * Date: 12-12-22
 * Version: 1.0
 */
package com.opensoft.common.event.core;

import com.opensoft.common.web.WebAppContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

/**
 * Description : event触发器
 * User : 康维
 */
@Deprecated
public class EventTrigger {
    private static final Logger log = LoggerFactory.getLogger(EventTrigger.class);

    /**
     * 触发事件
     *
     * @param eventName 事件
     * @param args      参数
     */
    public static void fire(final String eventName, final Object... args) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (log.isDebugEnabled()) {
                    log.debug("[CWF]-开始处理{}事件", eventName);
                }
                invoke(eventName, args);
            }
        };
        com.opensoft.common.pool.ThreadPoolManager.getEventThreadPool().submit(runnable);
    }

    private static void invoke(String eventName, Object... args) {
        HandlerLoader handler = com.opensoft.common.event.core.EventHandlersContainer.getEventHandler(eventName);
        if (handler != null) {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("[CWF]-获取到事件[{}]的Handler[{}]", eventName, handler.toString());
                }

                Object invoke;
                // 与Spring集成，如果声明为组件，则用Spring的组件进行调用
                Object bean = WebAppContextUtils.getWebAppContext().getApplicationContext().getBean(handler.getClazz());

                Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();
                if (parameterTypes.length > 0) {
                    invoke = ReflectionUtils.invokeMethod(handler.getMethod(), bean, args);
                } else {
                    invoke = ReflectionUtils.invokeMethod(handler.getMethod(), bean);
                }

                if (log.isDebugEnabled()) {
                    log.debug("[CWF]-事件[{}]的OnEvent接收者处理结果：{}", eventName, invoke);
                }

            } catch (Exception e) {
                //TODO:统一事件异常处理机制
                log.error(e.getMessage(), e);
            }
        } else {
            log.warn("[CWF]-事件[{}]没有指定接收者OnEvent，将不做任何处理", eventName);
        }
    }
}
