/**
 * ClassName: NullAvoidFilter
 * CopyRight: OpenSoft
 * Date: 13-1-15
 * Version: 1.0
 */
package com.opensoft.common.utils.json.jsonlib;

import net.sf.json.util.PropertyFilter;

/**
 * Description :
 *
 * @author : 康维
 */
public class NullAvoidFilter implements PropertyFilter {
    @Override
    public boolean apply(Object o, String s, Object o2) {
        return o2 == null;
    }
}
