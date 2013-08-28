/**
 * ClassName: CustomBootstrapCacheLoader
 * CopyRight: OpenSoft
 * Date: 12-12-18
 * Version: 1.0
 */
package com.opensoft.common.cache.ehcache;

import com.opensoft.common.utils.CollectionUtils;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.bootstrap.BootstrapCacheLoader;
import net.sf.ehcache.bootstrap.BootstrapCacheLoaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

/**
 * Description :
 * User : 康维
 */
public class CustomBootstrapCacheLoader extends BootstrapCacheLoaderFactory implements BootstrapCacheLoader {
    private static final Logger log = LoggerFactory.getLogger(CustomBootstrapCacheLoader.class);
    @Override
    public void load(Ehcache ehcache) throws CacheException {
        List keys = ehcache.getKeys();
        if (com.opensoft.common.utils.CollectionUtils.isEmpty(keys)) {
            return;
        }

        for (Object key : keys) {
            log.info("key=" + key + "重新加载到缓存");
            Element element = ehcache.getQuiet(key);
            ehcache.removeQuiet(key);
            ehcache.putQuiet(element);
        }
    }

    @Override
    public boolean isAsynchronous() {
        return false;
    }


    @Override
    public BootstrapCacheLoader createBootstrapCacheLoader(Properties properties) {
        return new CustomBootstrapCacheLoader();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
