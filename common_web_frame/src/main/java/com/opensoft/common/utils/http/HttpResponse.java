/**
 * ClassName: HttpRequest
 * CopyRight: OpenSoft
 * Date: 13-4-26
 * Version: 1.0
 */
package com.opensoft.common.utils.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Description : HTTP 响应
 *
 * @author : KangWei
 */
public class HttpResponse {
    private Map<String, String> header = new HashMap<String, String>();

    private String method;

    private int responseCode;

    private String responseMessage;

    private String content;

    public String getResponseMessage() {
        return responseMessage;
    }

    void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getMethod() {
        return method;
    }

    void setMethod(String method) {
        this.method = method;
    }

    public int getResponseCode() {
        return responseCode;
    }

    void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    void setHeader(String name, String value) {
        header.put(name, value);
    }

    public String getHeader(String name) {
        return header.get(name);
    }

    public String getContent() {
        return content;
    }

    void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "responseCode : " + responseCode +
                ", responseMessage : " + responseMessage +
                ", content : " + content;
    }
}
