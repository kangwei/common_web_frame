/**
 * ClassName: aaa
 * CopyRight: OpenSoft
 * Date: 13-4-9
 * Version: 1.0
 */
package com.opensoft.common.utils;

import java.io.UnsupportedEncodingException;

/**
 * Description : 汉字首字母生成工具
 *
 * @author : 康维
 */
public class PinYinUtil {
    private final static int finalA = 45217;
    private final static int finalB = 45253;
    private final static int finalC = 45761;
    private final static int finalD = 46318;
    private final static int finalE = 46826;
    private final static int finalF = 47010;
    private final static int finalG = 47297;
    private final static int finalH = 47614;
    private final static int finalJ = 48119;
    private final static int finalK = 49062;
    private final static int finalL = 49324;
    private final static int finalM = 49896;
    private final static int finalN = 50371;
    private final static int finalO = 50614;
    private final static int finalP = 50622;
    private final static int finalQ = 50906;
    private final static int finalR = 51387;
    private final static int finalS = 51446;
    private final static int finalT = 52218;
    private final static int finalW = 52698;
    private final static int finalX = 52980;
    private final static int finalY = 53689;
    private final static int finalZ = 54481;
    private final static int finalEnd = 55289;

    private static final int[] position = {
            finalA, finalB, finalC, finalD, finalE, finalF, finalG, finalH, finalJ,
            finalK, finalL, finalM, finalN, finalO, finalP, finalQ, finalR, finalS,
            finalT, finalW, finalX, finalY, finalZ, finalEnd
    };

    private static final char[] letter = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'w', 'x', 'y', 'z'
    };

    /**
     * 转换汉字为拼音首字母
     * @param sourceStr 源
     * @return 拼音首字母
     */
    public static String convert(String sourceStr) {
        if (StringUtils.isEmpty(sourceStr)) {
            return sourceStr;
        }
        StringBuilder builderStr = new StringBuilder();
        String result;
        for (int i = 0; i < sourceStr.length(); i++) {
            char tempChar = sourceStr.charAt(i);
            String tempStr = String.valueOf(tempChar);
            byte[] cnBytes;
            try {
                cnBytes = tempStr.getBytes("gb2312");
            } catch (UnsupportedEncodingException e) {
                return sourceStr;
            }
            if (cnBytes.length == 1) {
                builderStr.append(tempStr.toUpperCase());
            } else {
                builderStr.append(convert(cnBytes).toUpperCase());
            }

        }
        result = builderStr.toString();
        return result;
    }

    /**
     * 获取汉字词语拼音的首字母
     * @param source 源
     * @return 拼音的首字母
     */
    public static String getFirstPinYinLetter(String source) {
        String convert = convert(source);
        if (StringUtils.isNotEmpty(convert)) {
            return StringUtils.substring(convert, 0, 1);
        }
        return source;
    }

    private static String convert(byte[] cnBytes) {
        int a1 = (short) (cnBytes[0]) + 256;
        int a2 = (short) (cnBytes[1]) + 256;
        long iCnChar = a1 * 256 + a2;
        for (int i = 0; i < 23; i++) {
            if (iCnChar >= position[i] && iCnChar < position[i + 1]) {
                return String.valueOf(letter[i]);
            }
        }
        return "*";
    }

    public static void main(String s[]) throws Exception {
        System.out.println(getFirstPinYinLetter(""));
        System.out.println(getFirstPinYinLetter(null));
        System.out.println(getFirstPinYinLetter("爱的色放看记录"));
    }
}
