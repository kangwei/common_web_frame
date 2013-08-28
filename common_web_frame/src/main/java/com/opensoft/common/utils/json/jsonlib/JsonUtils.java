/**
 * ClassName: JsonUtils
 * CopyRight: OpenSoft
 * Date: 12-11-27
 * Version: 1.0
 */
package com.opensoft.common.utils.json.jsonlib;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;
import net.sf.json.util.PropertyFilter;

import java.util.*;

/**
 * Description :
 * User : 康维
 */
public class JsonUtils {
    static JsonConfig jsonConfig;

    public static final String TIME_STAMP_FORMAT_Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_STAMP_FORMAT_Y_M_D = "yyyy-MM-dd";
    public static final String TIME_STAMP_FORMAT_H_M_S = "HH:mm:ss";

    static {
        String[] formats = new String[]{TIME_STAMP_FORMAT_Y_M_D_H_M_S, TIME_STAMP_FORMAT_Y_M_D, TIME_STAMP_FORMAT_H_M_S};
        JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(formats));
        jsonConfig = JsonConfigFactory.buildJsonConfig(TIME_STAMP_FORMAT_Y_M_D_H_M_S);
    }

    public static String toJson(Object o) {
//        JsonConfig jsonConfig = JsonConfigFactory.buildJsonConfig(Constants.TIME_STAMP_FORMAT_Y_M_D_H_M_S);
        if (o instanceof Collection) {
            return JSONArray.fromObject(o, jsonConfig).toString();
        }
        return JSONObject.fromObject(o, jsonConfig).toString();
    }

    public static String toJson(Object o, JsonConfig jsonConfig) {
        //        JsonConfig jsonConfig = JsonConfigFactory.buildJsonConfig(Constants.TIME_STAMP_FORMAT_Y_M_D_H_M_S);
        if (o instanceof Collection) {
            return JSONArray.fromObject(o, jsonConfig).toString();
        }
        return JSONObject.fromObject(o, jsonConfig).toString();
    }

    public static String toJson(Object o, PropertyFilter... propertyFilters) {
        JsonConfig jsonConfig1 = new JsonConfig();
        jsonConfig1.registerJsonValueProcessor(Date.class, new DateProcessor(TIME_STAMP_FORMAT_Y_M_D_H_M_S));
        jsonConfig1.registerJsonValueProcessor(java.sql.Date.class, new DateProcessor(TIME_STAMP_FORMAT_Y_M_D_H_M_S));
        for (PropertyFilter propertyFilter : propertyFilters) {
            jsonConfig1.setJsonPropertyFilter(propertyFilter);
        }

        return toJson(o, jsonConfig1);
    }

    public static String toJson(Object o, String[] excludes, PropertyFilter... propertyFilters) {
        JsonConfig jsonConfig1 = new JsonConfig();
        jsonConfig1.setExcludes(excludes);
        jsonConfig1.registerJsonValueProcessor(Date.class, new DateProcessor(TIME_STAMP_FORMAT_Y_M_D_H_M_S));
        jsonConfig1.registerJsonValueProcessor(java.sql.Date.class, new DateProcessor(TIME_STAMP_FORMAT_Y_M_D_H_M_S));
        for (PropertyFilter propertyFilter : propertyFilters) {
            jsonConfig1.setJsonPropertyFilter(propertyFilter);
        }

        return toJson(o, jsonConfig1);
    }

    public static <T> List<T> toBeans(List<Map<String, Object>> items, Class clazz) {
        List<T> resultList = new ArrayList<T>();
        for (Map<String, Object> item : items) {
            resultList.add((T) JsonUtils.toBean(item, clazz));
        }
        return resultList;
    }

    public static <T> List<T> toBeans(List<Map<String, Object>> items, Class clazz, Map<String, Class> classMap) {
        List<T> resultList = new ArrayList<T>();
        for (Map<String, Object> item : items) {
            resultList.add((T) JsonUtils.toBean(item, clazz, classMap));
        }
        return resultList;
    }

    public static <T> List<T> toBeans(String json, Class clazz) {
        JSONArray array = JSONArray.fromObject(json);
        List<Map<String, Object>> items = JSONArray.fromObject(array);
        return toBeans(items, clazz);
    }

    public static <T> T toBean(String json, Class clazz) {
        return (T) JSONObject.toBean(JSONObject.fromObject(json), clazz);
    }

    public static <T> T toBean(Map<String, Object> map, Class clazz) {
        return (T) JSONObject.toBean(JSONObject.fromObject(map), clazz);
    }

    public static <T> T toBean(String json, Class clazz, Map<String, Class> classMap) {
        return (T) JSONObject.toBean(JSONObject.fromObject(json), clazz, classMap);
    }

    public static <T> T toBean(Map<String, Object> map, Class clazz, Map<String, Class> classMap) {
        return (T) JSONObject.toBean(JSONObject.fromObject(map), clazz, classMap);
    }

    public static Map<String, Object> fromJson(String json) {
        Map<String, Object> parameters = new HashMap<String, Object>();
//        JsonConfig jsonConfig = JsonConfigFactory.buildJsonConfig(Constants.TIME_STAMP_FORMAT_Y_M_D_H_M_S);
        try {
            Map<String, Object> map = JSONObject.fromObject(json, jsonConfig);
            for (String key : map.keySet()) {
                if (map.get(key) instanceof JSONObject) {  //对象类型处理
                    JSONObject jsonObject = (JSONObject) map.get(key);
                    Map<String, Object> obj = JSONObject.fromObject(jsonObject, jsonConfig);
                    parameters.put(key, obj);
                } else if (map.get(key) instanceof JSONArray) { //数组类型处理
                    JSONArray jsonArray = (JSONArray) map.get(key);
                    //构建一个list对象，保存数组
                    List<Object> innerList = new ArrayList<Object>();
                    for (Object aJsonArray : jsonArray) {
                        if (aJsonArray instanceof JSONObject) {
                            Map<String, Object> innerMap = (Map<String, Object>) aJsonArray;
                            innerList.add(innerMap);
                        } else if (aJsonArray instanceof String) {
                            innerList.add(aJsonArray);
                        } else if (aJsonArray instanceof Integer) {
                            innerList.add(aJsonArray);
                        }
                    }
                    parameters.put(key, innerList);

                } else if (map.get(key) instanceof JSONNull) {
                    parameters.put(key, null);
                } else {    //其他类型
                    parameters.put(key, map.get(key));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("解析请求中的json串失败", e);
        }

        return parameters;
    }
}
