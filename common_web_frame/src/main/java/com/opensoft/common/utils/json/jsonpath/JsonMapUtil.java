/**
 * ClassName: JsonMapUtil
 * CopyRight: TalkWeb
 * Date: 14-2-18
 * Version: 1.0
 */
package com.opensoft.common.utils.json.jsonpath;

import com.opensoft.common.utils.json.jsonlib.JsonUtils;

import java.util.Map;

/**
 * Description :
 *
 * @author : KangWei
 */
public class JsonMapUtil {
    public static Object getValue(String xpath, String json) {
        return null;
    }

    public static void main(String[] args) {
        String json = "{\"employee\":[" +
                "{ \"firstName\":\"John\" , \"lastName\":\"Doe\" }, " +
                "{ \"firstName\":\"Anna\" , \"lastName\":\"Smith\" }, " +
                "{ \"firstName\":\"Peter\" , \"lastName\":\"Jones\" }" +
                "]}";

        Map<String,Object> stringObjectMap = JsonUtils.fromJson(json);
        System.out.println(stringObjectMap);
    }
}
