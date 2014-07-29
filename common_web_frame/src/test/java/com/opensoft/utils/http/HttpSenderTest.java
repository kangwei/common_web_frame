/**
 * ClassName: HttpSenderTest
 * CopyRight: TalkWeb
 * Date: 2014/7/29
 * Version: 1.0
 */
package com.opensoft.utils.http;

import com.opensoft.common.utils.http.*;
import org.junit.Test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Description :
 *
 * @author : KangWei
 */
public class HttpSenderTest {
    @Test
    public void sslRequestTest() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        HttpConfig config = new HttpConfig();
        HttpSenderFactory.setConfig(config);
        HttpRequest request = new HttpRequest();
        HttpSender httpSender = HttpSenderFactory.newInstance("https://www.alipay.com/");
        HttpResponse response = httpSender.send(request);
        System.out.println(response);
    }
}
