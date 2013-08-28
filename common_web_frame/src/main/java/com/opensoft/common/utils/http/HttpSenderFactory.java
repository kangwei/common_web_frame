/**
 * ClassName: HttpSenderFactory
 * CopyRight: OpenSoft
 * Date: 13-4-26
 * Version: 1.0
 */
package com.opensoft.common.utils.http;

import java.io.IOException;

/**
 * Description : HttpSender工厂，获取HttpSender实例
 *
 * @author : KangWei
 */
public class HttpSenderFactory {
    private HttpSenderFactory() {
    }

    /**
     * HttpSender 的基础设置
     */
    private static HttpConfig config;

    /**
     * 取得HttpSender实例
     *
     * @param url 地址
     * @return HttpSender实例
     * @throws java.io.IOException 如果发生 I/O 异常，抛出
     */
    public static HttpSender newInstance(String url) throws IOException {
        return new HttpSender(url, config);
    }

    public static HttpConfig getConfig() {
        return config;
    }

    public static void setConfig(HttpConfig config) {
        HttpSenderFactory.config = config;
    }
}
