package com.opensoft.common.utils;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Description: 配置文件管理工具类
 *
 * @author KangWei
 * @Date 12-1-9
 */
public class PropertiesUtils {
    private static final Logger log = LoggerFactory.getLogger(PropertiesUtils.class);

    private static CompositeConfiguration config;
    
    

    /**
     * 读取配置文件到properties，一般在系统启动的时候加载
     */
    public static void readConfig() {
    	try {
        	String realPath =PropertiesUtils.class.getClassLoader().getResource("").toURI().getPath(); 
        	String envPath = System.getenv("WX_ENV_STR");
        	realPath +="properties/";
        	realPath += StringUtils.isEmpty(envPath)?"dev":envPath;
        	
            config = new CompositeConfiguration();
            File dictionary = new File(realPath);
            File[] files = dictionary.listFiles();
            if (files != null) {
                for (File propertiesFile : files) {
                	boolean garbageFile = propertiesFile.getName().endsWith(".svn");
                	if(garbageFile) continue;
                    PropertiesConfiguration configuration = new PropertiesConfiguration();
                    configuration.setEncoding("UTF-8");
                    configuration.load(propertiesFile);
                    config.addConfiguration(configuration);
                }
            }

            List<String> keys = listKeys();
            log.info("<----------------------读取配置文件成功----------------------->");
            for (String key : keys) {
                log.info("key[" + key + "] value[" + config.getProperty(key) + "]");
            }
            log.info("<----------------------------------------------------------->");
        } catch (Exception e) {
            log.error("读取配置文件失败", e);
        }
    }

    /**
     * 根据配置文件key读取值
     *
     * @param key key
     * @return 值
     */
    public static String getValue(String key) {
        return config.getString(key);
    }

    /**
     * 根据配置文件key读取值，没有取到返回默认值
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return 值
     */
    public static String getValue(String key, String defaultValue) {
        return config.getString(key, defaultValue);
    }

    /**
     * 获取整形配置值
     *
     * @param key key
     * @return 值
     */
    public static Integer getIntegerValue(String key) {
        return config.getInt(key);

    }

    /**
     * 获取整形配置值，如果没有取到，则用默认值
     *
     * @param key          key
     * @param defaultValue 默认值
     * @return 值
     */
    public static Integer getIntegerValue(String key, Integer defaultValue) {
        return config.getInt(key, defaultValue);
    }

    /**
     * 获取boolean型配置值
     *
     * @param key key
     * @return 值
     */
    public static boolean getBooleanValue(String key) {
        return config.getBoolean(key);
    }

    /**
     * 配置文件是否包含某个key
     *
     * @param key key
     * @return true-包含，false-不包含
     */
    public static boolean containsKey(String key) {
        return config.containsKey(key);
    }

    /**
     * 列出配置文件所有的key
     *
     * @return keys
     */
    public static List<String> listKeys() {
        List<String> list = new ArrayList<String>();
        Iterator keys = config.getKeys();
        while (keys.hasNext()) {
            list.add((String) keys.next());
        }

        return list;
    }
}
