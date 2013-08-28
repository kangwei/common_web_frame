/**
 * ClassName: CacheOperation
 * CopyRight: OpenSoft
 * Date: 13-6-9
 * Version: 1.0
 */
package com.opensoft.common.cache.interceptor;

import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Description :
 *
 * @author : KangWei
 */
class CacheOperation {
    public static final int CACHE = 1;
    public static final int PUT = 2;
    public static final int EVICT = 3;

    private Set<Class<?>> relatedClasses = Collections.emptySet();
    private String condition = "";
    private String key = "";
    private Boolean allClear = false;
    private Integer operation = CACHE;
    private Integer ttl;
    private Integer tti;

    public Integer getTtl() {
        return ttl;
    }

    public void setTtl(Integer ttl) {
        this.ttl = ttl;
    }

    public Integer getTti() {
        return tti;
    }

    public void setTti(Integer tti) {
        this.tti = tti;
    }

    public Set<Class<?>> getRelatedClasses() {
        return relatedClasses;
    }

    public String getCondition() {
        return condition;
    }

    public String getKey() {
        return key;
    }

    public void setRelatedClasses(Class<?>[] cacheClasses) {
        if (cacheClasses != null) {
            this.relatedClasses = new LinkedHashSet<Class<?>>(cacheClasses.length);
            Collections.addAll(this.relatedClasses, cacheClasses);
        }
    }

    public void setCondition(String condition) {
        Assert.notNull(condition);
        this.condition = condition;
    }

    public void setKey(String key) {
        Assert.notNull(key);
        this.key = key;
    }

    public Boolean getAllClear() {
        return allClear;
    }

    public void setAllClear(Boolean allClear) {
        this.allClear = allClear;
    }

    public Integer getOperation() {
        return operation;
    }

    public void setOperation(Integer operation) {
        this.operation = operation;
    }

    /**
     * This implementation compares the {@code toString()} results.
     *
     * @see #toString()
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof CacheOperation && toString().equals(other.toString()));
    }

    /**
     * This implementation returns {@code toString()}'s hash code.
     *
     * @see #toString()
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * Return an identifying description for this cache operation.
     * <p>Has to be overridden in subclasses for correct {@code equals}
     * and {@code hashCode} behavior. Alternatively, {@link #equals}
     * and {@link #hashCode} can be overridden themselves.
     */
    @Override
    public String toString() {
        return getOperationDescription().toString();
    }

    /**
     * Return an identifying description for this caching operation.
     * <p>Available to subclasses, for inclusion in their {@code toString()} result.
     */
    protected StringBuilder getOperationDescription() {
        StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName());
        result.append("[relatedClasses=");
        result.append(Arrays.toString(relatedClasses.toArray()));
        result.append(" | key='");
        result.append(this.key);
        result.append("' | condition='");
        result.append(this.condition);
        result.append("' | operation='");
        result.append(this.operation);
        result.append("']");
        return result;
    }
}
