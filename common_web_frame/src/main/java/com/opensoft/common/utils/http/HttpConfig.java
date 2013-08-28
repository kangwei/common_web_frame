/**
 * ClassName: HttpConfig
 * CopyRight: OpenSoft
 * Date: 13-4-26
 * Version: 1.0
 */
package com.opensoft.common.utils.http;

/**
 * Description : HTTP 基础配置参数
 *
 * @author : KangWei
 */
public class HttpConfig {
    private int connectTimeout = 10 * 1000;

    private int readTimeout = 30 * 1000;

    private String userName;

    private String password;

    public HttpConfig(){}

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
