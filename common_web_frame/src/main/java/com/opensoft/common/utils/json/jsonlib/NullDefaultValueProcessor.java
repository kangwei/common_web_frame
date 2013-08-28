/**
 * ClassName: NullDefaultValueProcessor
 * CopyRight: OpenSoft
 * Date: 13-1-14
 * Version: 1.0
 */
package com.opensoft.common.utils.json.jsonlib;

import net.sf.json.JSONNull;
import net.sf.json.processors.DefaultValueProcessor;

/**
 * Description :
 *
 * @author : 康维
 */
public class NullDefaultValueProcessor implements DefaultValueProcessor {
    @Override
    public Object getDefaultValue(Class aClass) {
        return JSONNull.getInstance();
    }
}
