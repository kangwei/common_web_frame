/**
 * ClassName: CacheClientFactory
 * CopyRight: OpenSoft
 * Date: 12-11-26
 * Version: 1.0
 */
package com.opensoft.common.cache;

/**
 * Description : cacheClient的工厂类，用于获取cacheClient
 * User : 康维
 */
@Deprecated
public class CacheClientFactory implements com.opensoft.common.StartAble {

    private CacheClient cacheClient;

    @Override
    public void start() {
        //nothing to do
    }

    @Override
    public void stop() {
        if (cacheClient != null) {
            cacheClient.stop();
        }
    }
}
