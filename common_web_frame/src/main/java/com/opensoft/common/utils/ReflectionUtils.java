/**
 * ClassName: ReflectionUtils
 * CopyRight: TalkWeb
 * Date: 13-9-4
 * Version: 1.0
 */
package com.opensoft.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Description :
 *
 * @author : KangWei
 */
public class ReflectionUtils {
    public static Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class superClass = clazz.getSuperclass();
            while (superClass != Object.class) {
                if (superClass == null) {
                    throw e;
                } else {
                    return getField(superClass, fieldName);
                }
            }
            throw e;
        }
    }

    public static void makeAccessible(Field field) {
        if (!Modifier.isPublic(field.getModifiers()) ||
                !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }
}
