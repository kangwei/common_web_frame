package com.opensoft.common.utils.http;

import com.opensoft.common.utils.CloseableUtils;
import com.opensoft.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 发送 HTTP 请求并返回结果。每个 HttpSender
 * 对象用于向一个地址发送，每次发送可以带不同的参数。
 * <p/>
 *
 * @author KangWei
 * @Date 12-4-14
 */
public class HttpSender {

    private static final Logger log = LoggerFactory.getLogger(HttpSender.class);

    private static int ncCount = 1;

    private static String realm;

    private static String nonce;

    private static String qop;

    private static boolean isContainAuthorization;

    private HttpConfig config;

    private String url; //url地址

    private HttpURLConnection connection; //connection

    private HttpRequest request;

    private boolean is_first_auth_request = true;

    public String getUrl() {
        return url;
    }

    public int getConnetTimeout() {
        return config.getConnectTimeout();
    }

    public int getReadTimeout() {
        return config.getReadTimeout();
    }

    public HttpConfig getConfig() {
        return config;
    }

    void setConfig(HttpConfig config) {
        this.config = config;
    }

    /**
     * 构造HttpSender实例
     *
     * @param url    请求地址
     * @param config 初始配置
     */
    HttpSender(String url, HttpConfig config) {
        this.url = url;
        setConfig(config);
    }

    public HttpResponse send(HttpRequest request) throws IOException {
        connection = (HttpURLConnection) new URL(url).openConnection();
        if ("GET".equals(request.getMethod()) || "DELETE".equals(request.getMethod())) {
            String queryString = buildQueryString(request);
            if (!url.contains("?")) {
                url = url + "?" + queryString;
            } else {
                if (url.endsWith("?")) {
                    url += queryString;
                } else {
                    url = url + "&" + queryString;
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("request uri:{}", url);
        }
        this.request = request;
        if ("POST".equals(request.getMethod())) {
            connection.setUseCaches(false);
        } else {
            connection.setUseCaches(request.isUseCache());
        }
        return _send();
    }

    private HttpResponse _send() throws IOException {
        connection.setRequestMethod(request.getMethod());
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setConnectTimeout(config.getConnectTimeout());
        connection.setReadTimeout(config.getReadTimeout());
        setRequestHeader(request.getHeader());
        OutputStream outputStream = null;
        if ("POST".equals(request.getMethod()) || "CACHE".equals(request.getMethod())) {
            outputStream = connection.getOutputStream();
            writeParams(request, outputStream);
        }
        try {
            int responseCode = connection.getResponseCode();
            if (HttpURLConnection.HTTP_UNAUTHORIZED == responseCode && is_first_auth_request) {
                return auth_send();
            } else {
                return readResponse(connection);
            }
        } finally {
            CloseableUtils.close(outputStream);
            connection.disconnect();
        }
    }

    private void setRequestHeader(Map<String, String> header) throws IOException {
        if (!header.containsKey("Authorization") && isContainAuthorization) {
            connection.setRequestProperty("Authorization", buildAuthorizationResponse(realm, nonce, qop));
        }
        for (String key : header.keySet()) {
            connection.setRequestProperty(key, header.get(key));
        }
    }

    private HttpResponse auth_send() throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("开始用户{}的认证...", config.getUserName());
        }
        String authorization = buildAuthorizationHeader();
        if (authorization != null) {
            connection = (HttpURLConnection) new URL(url).openConnection();
            is_first_auth_request = false;
            request.setHeader("Authorization", authorization);

            return _send();
        } else {
            return readResponse(connection);
        }
    }

    private String buildAuthorizationHeader() throws IOException {
        String headerField = connection.getHeaderField("WWW-Authenticate");
        if (StringUtils.isNotEmpty(headerField)) {
            realm = getValueFromAuthorization(headerField, "realm");
            nonce = getValueFromAuthorization(headerField, "nonce");
            qop = getValueFromAuthorization(headerField, "qop");

            if (log.isDebugEnabled()) {
                log.debug("realm:{},nonce:{},qop:{}", new Object[]{realm, nonce, qop});
            }
            return buildAuthorizationResponse(realm, nonce, qop);
        }

        return null;
    }

    private String buildAuthorizationResponse(String realm, String nonce, String qop) throws IOException {
        String userName = config.getUserName();
        String password = config.getPassword();
        String ha1 = com.opensoft.common.utils.DigestUtils.md5(userName + ":" + realm + ":" + password).toLowerCase();
        String requestMethod = connection.getRequestMethod();
        String url = connection.getURL().toString();
        String nc = StringUtils.complemented(8, ncCount++);
        String cnonce = com.opensoft.common.utils.DigestUtils.md5(SeqUtils.generateRandomNumber(6, 999999));

        if (log.isDebugEnabled()) {
            log.debug("method:{},url:{},nc:{}, cnonce:{}", new Object[]{requestMethod, url, nc, cnonce});
        }

        String ha2 = com.opensoft.common.utils.DigestUtils.md5(requestMethod + ":" + url).toLowerCase();

        String response = com.opensoft.common.utils.DigestUtils.md5(ha1 + ":" + nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + ha2).toLowerCase();
        if (log.isDebugEnabled()) {
            log.debug("ha1:{},ha2:{},response:{}", new Object[]{ha1, ha2, response});
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Digest ").append("username=\"").append(userName).append("\",")
                .append("realm=\"").append(realm).append("\",")
                .append("nonce=\"").append(nonce).append("\",")
                .append("uri=\"").append(url).append("\",")
                .append("qop=").append(qop).append(",")
                .append("nc=").append(nc).append(",")
                .append("cnonce=\"").append(cnonce).append("\",")
                .append("response=\"").append(response).append("\",");
        isContainAuthorization = true;
        return sb.toString();
    }

    private static String getValueFromAuthorization(String authorization, String key) {
        String patten = key + "=\"(.*?)\"";
        Pattern pattern = Pattern.compile(patten);
        Matcher matcher = pattern.matcher(authorization);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    /**
     * 读取HttpURLConnection返回
     *
     * @param connection HttpURLConnection
     * @throws java.io.IOException 异常
     */
    private HttpResponse readResponse(HttpURLConnection connection) throws IOException {
        HttpResponse response = new HttpResponse();
        int responseCode = connection.getResponseCode();
        response.setResponseCode(responseCode);
        response.setMethod(connection.getRequestMethod());
        response.setResponseMessage(connection.getResponseMessage());
        Map<String, List<String>> headerFields = connection.getHeaderFields();
        for (String key : headerFields.keySet()) {
            response.setHeader(key, connection.getHeaderField(key));
        }
        if (HttpURLConnection.HTTP_OK == responseCode) {
            InputStream inputStream = connection.getInputStream();
            try {
                response.setContent(read(inputStream));
            } finally {
                CloseableUtils.close(inputStream);
            }
        }

        return response;
    }

    /**
     * 参数写入
     *
     * @param outputStream 输出流
     * @throws java.io.IOException 异常
     */
    private void writeParams(HttpRequest request, OutputStream outputStream) throws IOException {
        String parameters = buildQueryString(request);

        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        try {
            if (StringUtils.isNotEmpty(parameters)) {
                if (log.isDebugEnabled()) {
                    log.debug("请求参数：" + parameters);
                }
                writer.write(parameters);
            }
            if (request.getInputString() != null) {
                if (log.isDebugEnabled()) {
                    log.debug("post到流的数据:" + request.getInputString());
                }
                writer.write(request.getInputString());
            }
            writer.flush();
        } finally {
            CloseableUtils.close(writer);
        }
    }

    private String buildQueryString(HttpRequest request) {
        String parameters = "";
        for (String key : request.getParameters().keySet()) {
            List<String> values = request.getParameters().get(key);
            for (String value : values) {
                if (value != null) {
                    parameters += key + "=" + StringUtils.urlEncode(value) + "&";
                }
            }
        }
        parameters = StringUtils.removeEnd(parameters, "&");  //移除string字符串最后的&
        return parameters;
    }

    /**
     * 读取返回值
     *
     * @param inputStream 输入流
     * @return 请求返回
     * @throws java.io.IOException 异常
     */
    private String read(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } finally {
            CloseableUtils.close(reader, inputStream);
        }

        return sb.toString();
    }
}

