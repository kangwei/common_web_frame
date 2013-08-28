package com.opensoft.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtils {

    public static final String ALL_DIGIT_REGEX_STR = "^[0-9]\\d*$";

    public static boolean match(String expression, String regEx) {
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(expression);
        return mat.find();
    }

    /**
     * 是否数字
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        return match(str, ALL_DIGIT_REGEX_STR);
    }

}
