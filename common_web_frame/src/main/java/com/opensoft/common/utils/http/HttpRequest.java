/**
 * ClassName: HttpRequest
 * CopyRight: OpenSoft
 * Date: 13-4-26
 * Version: 1.0
 */
package com.opensoft.common.utils.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description : HTTP 请求
 *
 * @author : KangWei
 */
public class HttpRequest {
    private Map<String, List<String>> parameters = new HashMap<String, List<String>>();
    private Map<String, String> header = new HashMap<String, String>();

    private String method = "GET";

    private boolean useCache = true;

    private String inputString;

    public boolean isUseCache() {
        return useCache;
    }

    public HttpRequest setUseCache(boolean useCache) {
        this.useCache = useCache;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public HttpRequest setParameter(String name, String... values) {
        if (values != null) {
            List<String> params = parameters.get(name);
            if (params == null) {
                params = new ArrayList<String>();
                parameters.put(name, params);
            }

            for (Object value : values) {
                if (value != null) {
                    params.add(String.valueOf(value));
                }
            }
        }
        return this;
    }

    public Map<String, List<String>> getParameters() {
        return parameters;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public HttpRequest setHeader(String name, String value) {
        header.put(name, value);
        return this;
    }

    public String getHeaderField(String name) {
        return header.get(name);
    }

    public String getInputString() {
        return inputString;
    }

    public void setInputString(String inputString) {
        this.inputString = inputString;
    }

    @Override
    public String toString() {
        if (inputString == null || "".equals(inputString)) {
            return "request : method=" + method +
                    ", header=" + header +
                    ", parameters=" + parameters;
        } else {
            return "request : method=" + method +
                    ", header=" + header +
                    ", parameters=" + parameters +
                    ", inputString=" + inputString;
        }
    }
}
