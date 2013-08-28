/**
 * ClassName: EventComponent
 * CopyRight: OpenSoft
 * Date: 12-12-25
 * Version: 1.0
 */
package com.opensoft.common.event.core;

import com.opensoft.common.event.annotation.Event;
import com.opensoft.common.utils.AnnotationScanTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Description : 事件组件，Spring方式启动和加载，可以通过配置使其启用或停用
 * User : 康维
 */
public class EventComponent implements com.opensoft.common.StartAble, ApplicationListener<ContextClosedEvent>, ApplicationContextAware {
    private static final Logger log = LoggerFactory.getLogger(EventComponent.class);

    /**
     * 单例模式
     */
    private static EventComponent eventComponent;

    /**
     * 事件线程池
     */
    private ThreadPoolTaskExecutor executor;

    /**
     * 事件扫描包路径
     */
    private String scanPackages;

    /**
     * spring上下文环境
     */
    private ApplicationContext context;

    /**
     * 构造函数私有化，防止新的实例产生
     */
    private EventComponent() {
    }

    /**
     * 获取自身的实例
     *
     * @return 实例
     */
    public static EventComponent getInstance() {
        if (eventComponent == null) {
            eventComponent = new EventComponent();
        }

        return eventComponent;
    }

    /**
     * 启动方法，扫描指定包路径下的事件，做参数的检查
     */
    @Override
    public void start() {
        List<String> packages = new ArrayList<String>();
        if (com.opensoft.common.utils.StringUtils.isNotEmpty(scanPackages)) {
            String[] split = scanPackages.split(",", -1);
            Collections.addAll(packages, split);
        }
        log.info("[CWF]-初始化[Event]组件...");
        Set<Method> methods = AnnotationScanTool.scanMethodByAnnotation(packages, com.opensoft.common.event.annotation.OnEvent.class);
        for (Method method : methods) {
            com.opensoft.common.event.annotation.OnEvent onEvent = method.getAnnotation(com.opensoft.common.event.annotation.OnEvent.class);
            if (EventHandlersContainer.exist(onEvent.eventName())) {
                throw new com.opensoft.common.exception.AppRuntimeException("事件" + onEvent.eventName() + "的接收者已存在");
            }
            EventHandlersContainer.addEventHandler(onEvent.eventName(), method.getDeclaringClass(), method);
        }

        Set<Method> sendMethods = AnnotationScanTool.scanMethodByAnnotation(packages, com.opensoft.common.event.annotation.Send.class);
        for (Method sendMethod : sendMethods) {
            com.opensoft.common.event.annotation.Send send = sendMethod.getAnnotation(com.opensoft.common.event.annotation.Send.class);
            validateParameters(sendMethod, send);
        }

        EventThreadPoolMonitor.setExecutor(executor);
    }

    /**
     * 事件发送者和事件接收者的参数检查，
     * 1、事件发送者和接收者参数类型一致，参数个数一致
     * 2、事件发送者的返回值时接收者的参数
     *
     * @param declaredMethod 发送者方法
     * @param send           发送者事件
     */
    private void validateParameters(Method declaredMethod, com.opensoft.common.event.annotation.Send send) {
        for (Event event : send.events()) {
            HandlerLoader eventHandler = EventHandlersContainer.getEventHandler(event.eventName());
            if (eventHandler != null) {

                if (event.parameterType().equals(com.opensoft.common.event.annotation.ParameterType.arg)) {
                    Class<?>[] parameterTypes = eventHandler.getMethod().getParameterTypes();
                    if (parameterTypes.length > 0) {
                        Class<?>[] sendParameterTypes = declaredMethod.getParameterTypes();
                        if (!Arrays.equals(parameterTypes, sendParameterTypes)) {
                            throw new com.opensoft.common.exception.AppRuntimeException(event.eventName() + "事件接收者" + eventHandler.getClazz() + ":" + eventHandler.getMethod() + "参数不符合，期望：" + Arrays.toString(sendParameterTypes) + "，实际：" + Arrays.toString(parameterTypes));
                        }
                    }
                } else {
                    Class<?>[] parameterTypes = eventHandler.getMethod().getParameterTypes();
                    Class<?> returnType = declaredMethod.getReturnType();
                    if (parameterTypes.length == 1) {
                        if (!parameterTypes[0].equals(returnType)) {
                            throw new com.opensoft.common.exception.AppRuntimeException(event.eventName() + "事件接收者" + eventHandler.getClazz() + ":" + eventHandler.getMethod() + "参数不符合，期望：" + returnType + "，实际：" + parameterTypes[0]);
                        }
                    } else {
                        throw new com.opensoft.common.exception.AppRuntimeException(event.eventName() + "事件接收者" + eventHandler.getClazz() + ":" + eventHandler.getMethod() + "参数个数不符合，期望：" + 1 + "，实际：" + parameterTypes.length);
                    }
                }
            } else {
                log.warn("[CWF]-初始化[Event]组件Send[{}]没有指定事件接收者", event.eventName());
            }
        }
    }

    /**
     * 触发事件
     *
     * @param eventName 事件
     * @param args      参数
     */
    public void fire(final String eventName, final Object... args) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (log.isDebugEnabled()) {
                    log.debug("[CWF]-开始处理{}事件", eventName);
                }
                invoke(eventName, args);
            }
        };
        executor.execute(runnable);
    }

    /**
     * 执行事件接收者方法
     *
     * @param eventName 事件名
     * @param args      参数
     */
    private void invoke(String eventName, Object... args) {
        HandlerLoader handler = EventHandlersContainer.getEventHandler(eventName);
        if (handler != null) {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("[CWF]-获取到事件[{}]的Handler[{}]", eventName, handler.toString());
                }

                Object invoke;
                // 与Spring集成，如果声明为组件，则用Spring的组件进行调用
                Object bean = context.getBean(handler.getClazz());

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

    @Override
    public void stop() {
        log.info("[CWF]-停止[Event]组件");
    }

    public ThreadPoolTaskExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(ThreadPoolTaskExecutor executor) {
        this.executor = executor;
    }

    public String getScanPackages() {
        return scanPackages;
    }

    public void setScanPackages(String scanPackages) {
        this.scanPackages = scanPackages;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    /**
     * 事件组件关闭方法
     *
     * @param event 关闭事件
     */
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        Map<String, ThreadPoolTaskExecutor> taskExecutorMap = context.getBeansOfType(ThreadPoolTaskExecutor.class);
        for (ThreadPoolTaskExecutor executor : taskExecutorMap.values()) {
            int retryCount = 0;
            while (executor.getActiveCount() > 0 && ++retryCount < 20) {
                try {
                    log.info("Executer " + executor.getThreadNamePrefix() + " is still working with active " + executor.getActiveCount() + " work. Retry count is " + retryCount);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    log.error("Executer shutdown error", e);
                }
            }
            if (!(retryCount < 20))
                log.info("Executer " + executor.getThreadNamePrefix() + " is still working.Since Retry count exceeded max value " + retryCount + ", will be killed immediately");
            executor.shutdown();
            log.info("Executer " + executor.getThreadNamePrefix() + " with active " + executor.getActiveCount() + " work has killed");
        }
    }
}
