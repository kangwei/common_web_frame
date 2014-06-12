/**
 * ClassName: CacheAop
 * CopyRight: OpenSoft
 * Date: 12-12-20
 * Version: 1.0
 */
package com.opensoft.common.cache.interceptor;

import com.opensoft.common.cache.CacheClient;
import com.opensoft.common.cache.annotation.Cache;
import com.opensoft.common.cache.annotation.CacheClear;
import com.opensoft.common.cache.annotation.CacheUpdate;
import com.opensoft.common.exception.AppRuntimeException;
import com.opensoft.common.spel.ExpressionEvaluator;
import com.opensoft.common.utils.DigestUtils;
import com.opensoft.common.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Description : Cache 拦截器
 * User : 康维
 */
@Aspect
public class CacheInterceptor {
    private static final Logger log = LoggerFactory.getLogger(CacheInterceptor.class);

    private ExpressionEvaluator evaluator = new ExpressionEvaluator();

    @Autowired
    CacheClient cacheClient;

    /**
     * 拦截点，Cache，CacheClear，CacheUpdate注解拦截
     */
    @Pointcut("@annotation(com.opensoft.common.cache.annotation.Cache) " +
            "|| @annotation(com.opensoft.common.cache.annotation.CacheClear) " +
            "|| @annotation(com.opensoft.common.cache.annotation.CacheUpdate)")
    protected void cacheMethod() {

    }

    @Around("cacheMethod()")
    public Object invoke(ProceedingJoinPoint pjp) throws IOException {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Object target = pjp.getTarget();
        Class<?> targetClass = target.getClass();
        Object[] args = pjp.getArgs();
        if (log.isDebugEnabled()) {
            log.debug("[CWF]-Cache源类{}，源目标{}，源方法{}，源参数{}", new Object[]{
                    targetClass, target.toString(), method.getName(), Arrays.toString(args)
            });
        }
        CacheOperation cacheOperation = getCacheOperation(method);
        if (log.isDebugEnabled()) {
            log.debug("[CWF]-Cache操作{}", cacheOperation.toString());
        }
        String defaultCacheName = cacheOperation.getCacheName();
        if (StringUtils.isEmpty(defaultCacheName)) {
            defaultCacheName = generateCacheName(pjp.getTarget().getClass());
        }
        String  defaultCacheKey = generateKey(target, targetClass, method, cacheOperation, args);

        if (CacheOperation.CACHE == cacheOperation.getOperation()) {
            Object proceed;
            proceed = cacheClient.getFromCache(defaultCacheName, defaultCacheKey);
            if (proceed == null) {
                proceed = process(pjp);
                if (evaluator.isConditionPassing(target, targetClass, method, args, cacheOperation.getCondition())) {
                    handleRelatedCache(cacheOperation, defaultCacheName, defaultCacheKey, cacheClient, proceed);
                }
            }
            return proceed;
        } else if (CacheOperation.PUT == cacheOperation.getOperation()) {
            Object proceed = process(pjp);
//            Annotation[][] parameterAnnotations = method.getParameterAnnotations();

            if (args != null && args.length == 1) {
                if (evaluator.isConditionPassing(target, targetClass, method, args, cacheOperation.getCondition())) {
                    handleRelatedCache(cacheOperation, defaultCacheName, defaultCacheKey, cacheClient, args[0]);
                }
            } else {
                log.warn("CacheUpdate参数不符合期望，期望1，实际{}", args == null ? 0 : args.length);
            }
            return proceed;
        } else if (CacheOperation.EVICT == cacheOperation.getOperation()) {
            Object proceed = process(pjp);
            if (evaluator.isConditionPassing(target, targetClass, method, args, cacheOperation.getCondition())) {
                evictCache(cacheOperation, defaultCacheName, defaultCacheKey, cacheClient);
            }
            return proceed;
        } else {
            return process(pjp);
        }
    }

    /**
     * 清除缓存
     *
     * @param cacheOperation   缓存操作，是否全部清除
     * @param defaultCacheName 默认的CacheName
     * @param defaultCacheKey  默认的CacheKey
     * @param cacheClient      cache客户端
     */
    private void evictCache(CacheOperation cacheOperation, String defaultCacheName, Object defaultCacheKey, CacheClient cacheClient) {
        if (cacheOperation.getAllClear()) {
            cacheClient.removeCache(defaultCacheName);
        } else {
            cacheClient.removeElement(defaultCacheName, defaultCacheKey);
        }
    }

    private void handleRelatedCache(CacheOperation cacheOperation, String defaultCacheName, Object defaultCacheKey, CacheClient cacheClient, Object cacheValue) throws IOException {
        cacheClient.putIntoCache(defaultCacheName, defaultCacheKey, cacheValue, cacheOperation.getTtl(), cacheOperation.getTti());
    }

    private Object process(ProceedingJoinPoint pjp) {
        Object proceed;
        try {
            proceed = pjp.proceed();
        } catch (Throwable throwable) {
            throw new AppRuntimeException(throwable);
        }
        return proceed;
    }

    private CacheOperation getCacheOperation(Method method) {
        CacheOperation cacheOperation = new CacheOperation();
        if (method.isAnnotationPresent(Cache.class)) {
            Cache cache = method.getAnnotation(Cache.class);
            cacheOperation.setCacheName(cache.cacheName());
            cacheOperation.setKey(cache.key());
            cacheOperation.setCondition(cache.condition());
            cacheOperation.setOperation(CacheOperation.CACHE);
            cacheOperation.setTtl(cache.timeToLive());
            cacheOperation.setTti(cache.timeToIdle());
        } else if (method.isAnnotationPresent(CacheClear.class)) {
            CacheClear cacheClear = method.getAnnotation(CacheClear.class);
            cacheOperation.setCacheName(cacheClear.cacheName());
            cacheOperation.setKey(cacheClear.key());
            cacheOperation.setCondition(cacheClear.condition());
            cacheOperation.setAllClear(cacheClear.allClear());
            cacheOperation.setOperation(CacheOperation.EVICT);
        } else if (method.isAnnotationPresent(CacheUpdate.class)) {
            CacheUpdate cacheUpdate = method.getAnnotation(CacheUpdate.class);
            cacheOperation.setCacheName(cacheUpdate.cacheName());
            cacheOperation.setKey(cacheUpdate.key());
            cacheOperation.setCondition(cacheUpdate.condition());
            cacheOperation.setOperation(CacheOperation.PUT);
            cacheOperation.setTtl(cacheUpdate.timeToLive());
            cacheOperation.setTti(cacheUpdate.timeToIdle());
        }
        return cacheOperation;
    }

    private String generateCacheName(Class cacheClass) {
        String cacheKey;
        Package aPackage = cacheClass.getPackage();
        cacheKey = aPackage.getName() + cacheClass.getName();
        try {
            return DigestUtils.md5(cacheKey);
        } catch (IOException e) {
            //do nothing
        }

        return null;
    }

    public String generateKey(Object target, Class<?> targetClass, Method method, CacheOperation operation, Object[] params) throws IOException {
        String operationKey = operation.getKey();
        if (StringUtils.isNotEmpty(operationKey)) {
            EvaluationContext evaluationContext = evaluator.createEvaluationContext(method, params,
                    target, targetClass, ExpressionEvaluator.NO_RESULT);
            return String.valueOf(evaluator.key(operationKey, method, evaluationContext));
        }
        StringBuilder sb = new StringBuilder();
        for (Object param : params) {
            sb.append(String.valueOf(param));
        }
        return sb.toString();
    }
}
