/**
 * ClassName: EventInterceptor
 * CopyRight: OpenSoft
 * Date: 12-12-22
 * Version: 1.0
 */
package com.opensoft.common.event.interceptor;

import com.opensoft.common.event.annotation.Event;
import com.opensoft.common.event.annotation.ParameterType;
import com.opensoft.common.event.annotation.Send;
import com.opensoft.common.event.core.EventComponent;
import com.opensoft.common.exception.AppRuntimeException;
import com.opensoft.common.spel.ExpressionEvaluator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

/**
 * Description : event拦截器
 * User : 康维
 */
@Aspect
public class EventInterceptor {
    private static final Logger log = LoggerFactory.getLogger(EventInterceptor.class);

    @Autowired
    EventComponent eventComponent;

    private ExpressionEvaluator evaluator = new ExpressionEvaluator();

    /**
     * Event拦截，拦截所有注解为Send的方法，触发事件
     *
     * @param pjp  方法代理
     * @param send Send
     * @return 方法返回值
     * @throws com.opensoft.common.exception.AppRuntimeException 异常
     */
    @Around("@annotation(send)")
    public Object invoke(ProceedingJoinPoint pjp, Send send) throws com.opensoft.common.exception.AppRuntimeException {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Object target = pjp.getTarget();
        Class<?> targetClass = target.getClass();
        Object[] args = pjp.getArgs();
        //执行方法
        Object proceed;
        try {
            proceed = pjp.proceed(args);
        } catch (Throwable throwable) {
            throw new com.opensoft.common.exception.AppRuntimeException(throwable);
        }
        for (com.opensoft.common.event.annotation.Event event : send.events()) {
            if (evaluator.isConditionPassing(target, targetClass, method, args, event.fireCondition())) {
                if (com.opensoft.common.event.annotation.ParameterType.arg.equals(event.parameterType())) {
                    eventComponent.fire(event.eventName(), args);
                } else {
                    eventComponent.fire(event.eventName(), proceed);
                }
            }
        }
        return proceed;
    }
}
