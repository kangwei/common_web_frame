/**
 * ClassName: PropertyLowCaseConverter
 * CopyRight: OpenSoft
 * Date: 12-12-3
 * Version: 1.0
 */
package com.opensoft.common.utils.json.jsonlib;

import net.sf.json.processors.PropertyNameProcessor;

/**
 * Description : 属性名小写转换器
 * User : 康维
 */
public class PropertyLowCaseConverter implements PropertyNameProcessor {

    @Override
    public String processPropertyName(Class aClass, String s) {
        return s.toLowerCase();
    }
}
