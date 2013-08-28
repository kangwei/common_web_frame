/**
 * ClassName: JsonPathUtils
 * CopyRight: OpenSoft
 * Date: 13-4-3
 * Version: 1.0
 */
package com.opensoft.common.utils.json.jsonpath;

import com.nebhale.jsonpath.JsonPath;
import com.opensoft.common.utils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Description : 用xpath解析json
 *
 * @author : 康维
 */
public class JsonPathUtils {
    public static <T> T getValue(String expression, String json, Class<T> classType) {
        return JsonPath.read(expression, json, classType);
    }

    public static <T> T toBean(String expression, String json, Class<T> classType) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Map<String, Object> map = JsonPath.read(expression, json, Map.class);
        T t = classType.newInstance();
        BeanUtils.populate(t, map);
        return t;
    }
}
