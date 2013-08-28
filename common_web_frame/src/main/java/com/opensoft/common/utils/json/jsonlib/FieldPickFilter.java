/**
 * ClassName: FieldPickFilter
 * CopyRight: OpenSoft
 * Date: 13-5-6
 * Version: 1.0
 */
package com.opensoft.common.utils.json.jsonlib;

import net.sf.json.util.PropertyFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Description :
 *
 * @author : KangWei
 */
public class FieldPickFilter implements PropertyFilter {
    private static final Logger log = LoggerFactory.getLogger(FieldPickFilter.class);

    private Set<String> fields;

    public FieldPickFilter(Set<String> fields) {
        this.fields = fields;
    }

    @Override
    public boolean apply(Object o, String s, Object o2) {
        if (log.isDebugEnabled()) {
            log.debug("Object:{}, propertyName:{}, ObjectValue:{}", new Object[]{o, s, o2});
        }
        if (com.opensoft.common.utils.CollectionUtils.isNotEmpty(fields)) {

            if (log.isDebugEnabled()) {
                log.debug("需要获取的fields：{}", fields);
            }
            return !fields.contains(s);
        }

        return false;
    }
}
