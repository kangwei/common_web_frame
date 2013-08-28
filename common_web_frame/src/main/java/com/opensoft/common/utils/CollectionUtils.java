package com.opensoft.common.utils;

import org.apache.commons.collections.Predicate;

import java.util.*;

/**
 * Description:集合操作工具类
 *
 * @author KangWei
 * @Date 12-1-10
 */
public class CollectionUtils extends org.apache.commons.collections.CollectionUtils {
    /**
     * 从集合中取符合条件的元素的新集合
     *
     * @param list      集合
     * @param predicate 条件
     * @param <T>       泛型
     * @return 新集合
     */
    public static <T> List<T> filterList(List<T> list, Predicate predicate) {
        List<T> result = new ArrayList<T>();
        for (T t : list) {
            if (predicate.evaluate(t)) {
                result.add(t);
            }
        }

        return result;
    }

    /**
     * 从集合中取符合条件的元素的新集合
     *
     * @param list      集合
     * @param predicate 条件
     * @param size      新集合条数
     * @param <T>       泛型
     * @return 新集合
     */
    public static <T> List<T> filterList(List<T> list, Predicate predicate, int size) {
        if (size <= 0) {
            return null;
        }

        List<T> result = new ArrayList<T>();
        for (T t : list) {
            if (predicate.evaluate(t)) {
                result.add(t);
                size--;
                if (size == 0) {
                    return result;
                }
            }
        }

        return result;
    }

    /**
     * 从集合中取符合条件的第一个元素
     *
     * @param list      集合
     * @param predicate 条件
     * @param <T>       泛型
     * @return 集合中符合条件的第一个元素，没有则返回null
     */
    public static <T> T filterObject(List<T> list, Predicate predicate) {
        for (T t : list) {
            if (predicate.evaluate(t)) {
                return t;
            }
        }

        return null;
    }

    /**
     * 过滤重复记录
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> filterDuplicate(List<T> list) {
        Set<T> set = new HashSet<T>();

        set.addAll(list);

        List<T> returnList = new ArrayList<T>();
        returnList.addAll(set);
        return returnList;
    }

    /**
     * 获取集合target不在集合source中的元素
     *
     * @param source 源
     * @param target 目标
     * @return
     */
    public static <T> LinkedList<T> filterElementNotInSource(Collection<? extends T> source, Collection<? extends T> target) {
        //使用LinkList防止差异过大时,元素拷贝
        LinkedList<T> returnCollection = new LinkedList<T>();

        //直接指定大小,防止再散列
        Map<T, Integer> map = new HashMap<T, Integer>(source.size());
        for (T t : source) {
            map.put(t, 1);
        }
        for (T t : target) {
            if (map.get(t) == null) {
                returnCollection.add(t);
            }
        }
        return returnCollection;
    }

    /**
     * 获取集合target不在集合source中的元素,去除重复
     *
     * @param source 源
     * @param target 目标
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> LinkedList<T> filterElementNotInSourceNoDuplicate(Collection<? extends T> source, Collection<? extends T> target) {
        Set<T> set = new HashSet(filterElementNotInSource(source, target));
        return new LinkedList<T>(set);
    }

    /**
     * 从集合中取指定条数
     *
     * @param list list
     * @param size 条数
     * @param <T>  泛型
     * @return 集合
     */
    public static <T> List<T> first(List<T> list, int size) {
        if (CollectionUtils.isNotEmpty(list)) {
            if (list.size() <= size) {
                return list;
            } else {
                return list.subList(0, size);
            }
        } else {
            return null;
        }
    }

    /**
     * 转换集合
     *
     * @param t   一个或多个元素
     * @param <T> 泛型
     * @return 集合
     */
    public static <T> List<T> asList(T... t) {
        List<T> list = new ArrayList<T>();
        Collections.addAll(list, t);

        return list;
    }

    /**
     * 转换set到List
     *
     * @param set set
     * @param <T> 泛型
     * @return list
     */
    public static <T> List<T> setToList(Set<T> set) {
        List<T> list = new ArrayList<T>();
        list.addAll(set);
        return list;
    }

    /**
     * clone一个集合
     *
     * @param list 待clone集合
     * @param <T>  泛型
     * @return clone集合
     */
    public static <T> List<T> cloneList(List<T> list) {
        List<T> newList = new ArrayList<T>();
        for (T t : list) {
            newList.add(t);
        }

        return newList;
    }
}
