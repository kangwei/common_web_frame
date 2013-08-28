/**
 * ClassName: JsonConfigFactory
 * CopyRight: OpenSoft
 * Date: 12-11-24
 * Version: 1.0
 */
package com.opensoft.common.utils.json.jsonlib;

import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import java.util.Date;

/**
 * Description : JsonConfig的工厂类，获取唯一的JsonConfig，用于json序列化和反序列化用
 * User : 康维
 */
public class JsonConfigFactory {
    private static JsonConfig jsonConfig = new JsonConfig();

    public static JsonConfig buildJsonConfig(String datePatten) {
        jsonConfig.registerJsonValueProcessor(Date.class, new DateProcessor(datePatten));
        jsonConfig.registerJsonValueProcessor(java.sql.Date.class, new DateProcessor(datePatten));
        return jsonConfig;
    }

    public static JsonConfig registerDefaultProcessor() {
//        jsonConfig.registerDefaultValueProcessor(Integer.class, new NullDefaultValueProcessor());
        jsonConfig.registerDefaultValueProcessor(String.class, new com.opensoft.common.utils.json.jsonlib.NullDefaultValueProcessor());
//        jsonConfig.registerDefaultValueProcessor(Long.class, new NullDefaultValueProcessor());
//        jsonConfig.registerDefaultValueProcessor(Float.class, new NullDefaultValueProcessor());
//        jsonConfig.registerDefaultValueProcessor(Boolean.class, new NullDefaultValueProcessor());
//        jsonConfig.registerDefaultValueProcessor(Double.class, new NullDefaultValueProcessor());

        return jsonConfig;
    }

    public static JsonConfig setFilter(PropertyFilter... propertyFilter) {
        for (PropertyFilter filter : propertyFilter) {
            jsonConfig.setJsonPropertyFilter(filter);
        }

        return jsonConfig;
    }

    public static JsonConfig propertyConverter(Class... classes) {
        for (Class aClass : classes) {
            jsonConfig.setRootClass(aClass);
            jsonConfig.registerJavaPropertyNameProcessor(aClass, new com.opensoft.common.utils.json.jsonlib.PropertyLowCaseConverter());
        }

        return jsonConfig;
    }
}
