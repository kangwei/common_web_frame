/**
 * ClassName: ComponentManager
 * CopyRight: OpenSoft
 * Date: 12-12-25
 * Version: 1.0
 */
package com.opensoft.common;

import com.opensoft.common.exception.AppRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description : 组件管理器
 * User : 康维
 */
@Deprecated
public class ComponentManager {
    private static final Logger log = LoggerFactory.getLogger(ComponentManager.class);
    public static final String EVENT = "event";

    private static Map<String, StartAble> components = new ConcurrentHashMap<String, StartAble>();

    public static void registerComponent(String componentName, StartAble component) {
        components.put(componentName, component);
    }

    public static StartAble getComponent(String componentName) {
        StartAble startable = components.get(componentName);
        if (startable == null) {
            throw new AppRuntimeException("组件" + componentName + "未被注册");
        }
        return startable;
    }

    public static void removeComponent(String componentName) {
        components.remove(componentName);
    }
}
