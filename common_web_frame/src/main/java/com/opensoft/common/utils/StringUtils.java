package com.opensoft.common.utils;

import java.io.*;
import java.net.URLEncoder;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: 字符串操作工具类，继承apache的StringUtils
 *
 * @author KangWei
 * @Date 11-12-28
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {
    /**
     * 批量替换
     *
     * @param source 源字符串
     * @param with   替换字符串
     * @param repls  需替换的字符串列表
     * @return 替换后的字符串
     */
    public static String replaceStr(String source, String with, String... repls) {
        for (String repl : repls) {
            source = StringUtils.replace(source, repl, with);
        }

        return source;
    }

    public static String regexReplace(String soucre, String with, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(soucre);
        while (matcher.find()) {
            soucre = StringUtils.replace(soucre, matcher.group(), with);
        }

        return soucre;
    }

    /**
     * @param @param oldChar 原字符
     * @param @param newChar 替换的字符
     * @param @param strArr 原来的字符串数组
     * @return String[] 返回的字符串数组
     * @throws
     * @Title: replaceStrArr
     * @Description: 批量替换字符串
     */
    public static String[] replaceStrArr(String oldChar, String newChar, String[] strArr) {
        String[] strArray = new String[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            strArray[i] = strArr[i].replace(oldChar, newChar);
        }
        return strArray;
    }

    /**
     * ip校验
     *
     * @param s s
     * @return true or false
     */
    public static Boolean isIpAddress(String s) {
        String regex = "(((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))[.](((2[0-4]\\d)|(25[0-5]))|(1\\d{2})|([1-9]\\d)|(\\d))";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 判断是否是手机号码
     *
     * @param source 待检查字符串
     * @return 是返回true，否则返回false
     */
    public static boolean isChinaMobile(String source) {
        return validate(source, "(13[0-9]\\d{8})|(15[0-9]\\d{8})|(18[0-9]\\d{8}|14[0-9]\\d{8})");
    }

    /**
     * 根据给定的正则表达式判断字符串是否合法
     *
     * @param source 待检查字符串
     * @param regex  正则表达式
     * @return 是返回true，否则返回false
     */
    public static boolean validate(String source, String regex) {
        return source != null && source.matches(regex);
    }

    /**
     * 将指定的字符串进行 URL 编码，用于生成 URL 或参数
     *
     * @param message 要进行编码的字符串
     * @return 编码后的字符串
     */
    public static String urlEncode(String message) {
        try {
            return URLEncoder.encode(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }

    /**
     * @param @param  step 步长
     * @param @param  numberStr 数字字符串
     * @param @return
     * @return String
     * @throws
     * @Title: splitNumber
     * @Description: 将数字以逗号隔开
     */
    public static String splitNumber(int step, String numberStr) {
        String str = "";
        int count = numberStr.length() / step;
        if (numberStr.length() % step != 0) {
            count = count + 1;
        }
        for (int i = 0; i < count; i++) {
            int start = numberStr.length() - (i * step + step);
            int end = numberStr.length() - (i * step);
            if (start < 0) {
                start = 0;
            }
            str = "," + numberStr.substring(start, end) + str;
        }
        if (str.startsWith(",")) {
            str = str.substring(1, str.length());
        }
        return str;
    }

    /**
     * 返回空字符串
     *
     * @param str
     * @return
     */
    public static String getEmptyString(Object str) {
        if (str == null) {
            return "";
        }
        return str.toString();
    }

    /**
     * 产生一个随机的字符串
     *
     * @param length 字符串长度
     * @return
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 对序列进行格式化输出，不足位数前面补0
     *
     * @param digit  补全位数
     * @param seqNum 输出值
     * @return 序列
     */
    public static String complemented(int digit, int seqNum) {
        return String.format("%0" + digit + "d", seqNum);
    }

    public static boolean isNotEmptyAndNotStringNull(String str) {
        return isNotEmpty(str) && !"null".equals(str);
    }

    /**
     * 输入流转换为字符串
     *
     * @param inputStream 输入流
     * @param charsetName 字符编码
     * @return 字符串
     * @throws java.io.IOException IO异常
     */
    public static String streamToString(InputStream inputStream, String charsetName) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, charsetName));

            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } finally {
            CloseableUtils.close(reader);
        }
        return removeEnd(builder.toString(), "\n");
    }

    /**
     * 验证json串是否符合格式
     *
     * @param json json串
     * @return 符合-true
     */
    public static boolean validateJson(String json) {
        return StringUtils.validate(json, "\\{.*\\}") || StringUtils.validate(json, "\\[.*\\]");
    }

    /**
     * 过滤html代码
     *
     * @param inputString :
     *                    source string
     * @return String
     */
    public static String filterHtml(String inputString) {
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;

        Pattern p_html1;
        Matcher m_html1;

        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>  
            // }  
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>  
            // }  
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式  
            String regEx_html1 = "<[^>]+";
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签  

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签  

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签  

            p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
            m_html1 = p_html1.matcher(htmlStr);
            htmlStr = m_html1.replaceAll(""); // 过滤html标签  

            textStr = htmlStr;

        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }

        return textStr;// 返回文本字符串  
    }
}
