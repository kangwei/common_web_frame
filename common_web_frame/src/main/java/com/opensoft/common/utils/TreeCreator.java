/**
 * ClassName: TreeCreator
 * CopyRight: TalkWeb
 * Date: 13-8-28
 * Version: 1.0
 */
package com.opensoft.common.utils;

import com.opensoft.common.exception.AppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Description : 树构造器，辅助基于树状表结构的树构造，算法复杂度O(n*n)(可否优化)
 *
 * @author : KangWei
 */
public class TreeCreator {
    private static final Logger log = LoggerFactory.getLogger(TreeCreator.class);

    /**
     * 根据结果集生成树，仅支持主键，父键为基本类型，孩子为List类型的树生成
     *
     * @param ds                 数据结果集
     * @param parentNode         父节点
     * @param keyFieldName       主键名
     * @param parentKeyFieldName 父键名
     * @param childrenFieldName  孩子名
     * @param <T>                泛型
     * @return 树
     * @throws AppException 生成树异常抛出
     */
    public static <T> T createTree(List<T> ds, T parentNode, String keyFieldName, String parentKeyFieldName, String childrenFieldName) throws AppException {
        if (CollectionUtils.isEmpty(ds)) {
            return null;
        }

        if (parentNode == null
                || StringUtils.isEmpty(keyFieldName)
                || StringUtils.isEmpty(parentKeyFieldName)
                || StringUtils.isEmpty(childrenFieldName)) {
            throw new AppException("有方法参数为null，不符合期望");
        }

        try {
            Field keyField = parentNode.getClass().getDeclaredField(keyFieldName);
            validatePrimitiveField(keyFieldName, keyField);
            Field pKeyField = parentNode.getClass().getDeclaredField(parentKeyFieldName);
            validatePrimitiveField(parentKeyFieldName, pKeyField);
            Field childrenField = parentNode.getClass().getDeclaredField(childrenFieldName);
            if (!List.class.isAssignableFrom(childrenField.getType())) {
                throw new AppException(childrenFieldName + "参数不符合期望，期望List");
            }
        } catch (NoSuchFieldException e) {
            throw new AppException(e);
        }
        ds.remove(parentNode);
        return createTreeInner(ds, parentNode, keyFieldName, parentKeyFieldName, childrenFieldName);
    }

    /**
     * 递归创建树
     *
     * @param ds                 数据结果集
     * @param t                  父节点
     * @param keyFieldName       主键名
     * @param parentKeyFieldName 父键名
     * @param childrenFieldName  孩子名
     * @param <T>                泛型
     * @return 树
     * @throws AppException 生成树异常抛出
     */
    private static <T> T createTreeInner(List<T> ds, T t, String keyFieldName, String parentKeyFieldName, String childrenFieldName) throws AppException {
        try {
            Field childrenField = t.getClass().getDeclaredField(childrenFieldName);
            childrenField.setAccessible(true);
            List<T> children = (List<T>) childrenField.get(t);
            if (children == null) {
                children = new ArrayList<T>();
                BeanUtils.setProperty(t, childrenFieldName, children);
                if (log.isDebugEnabled()) {
                    log.debug("{}节点的孩子接口为空，将初始化...", BeanUtils.getSimpleProperty(t, keyFieldName));
                }
            }

            for (Iterator<T> iterator = ds.iterator(); iterator.hasNext(); ) {
                T d = iterator.next();
                if (log.isDebugEnabled()) {
                    log.debug("当前节点的父节点：{}，父节点：{}", BeanUtils.getSimpleProperty(d, parentKeyFieldName), BeanUtils.getSimpleProperty(t, keyFieldName));
                }
                if (BeanUtils.getSimpleProperty(d, parentKeyFieldName).equals(BeanUtils.getSimpleProperty(t, keyFieldName))) {
                    children.add(d);
                    createTreeInner(ds, d, keyFieldName, parentKeyFieldName, childrenFieldName);
                }
            }
        } catch (Exception e) {
            throw new AppException(e);
        }

        return t;
    }

    /**
     * 验证field是否是基本类型
     *
     * @param keyFieldName fieldName
     * @param keyField     field
     * @throws AppException 不是基本类型抛出异常
     */
    private static void validatePrimitiveField(String keyFieldName, Field keyField) throws AppException {
        /*if (!keyField.getType().isPrimitive()) {
            throw new AppException(keyFieldName + "参数不符合期望，期望基本类型");
        }*/
    }
}
