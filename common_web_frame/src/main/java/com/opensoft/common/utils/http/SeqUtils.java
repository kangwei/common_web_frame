/**
 * SeqUtils
 * Date: 12-6-21
 * Copyright: OpenSoft
 * Version: 1.0
 */
package com.opensoft.common.utils.http;

import com.opensoft.common.utils.StringUtils;

import java.util.Random;

/**
 * Description: 模拟一个序列
 *
 * @author : kangwei
 */
public class SeqUtils {
    private static int seqNum;  //序列计数

    private static final int MIN = 1;   //序列最小值
    private static final int MAX = 99999999;  //序列最大值
    private static final int increment = 1; //序列增量
    private static final boolean isRepeat = true;   //是否循环

    /**
     * 获取序列的下一个值
     *
     * @return 下一个值
     */
    public static String nextVal() {
        //判断当前计数是否已到最大值
        if (seqNum >= MAX) {
            //根据循环标记做相应处理
            if (isRepeat) {
                seqNum = MIN;
                return StringUtils.complemented(8, seqNum);
            } else {
                throw new IllegalArgumentException("序列已到最大值");
            }
        } else {
            seqNum += increment;
            return StringUtils.complemented(8, seqNum);
        }
    }

    /**
     * 生成随机数
     *
     * @param seed 种子
     * @return 随机数
     */
    private static int random(int seed) {
        Random random = new Random();
        return random.nextInt(seed);
    }

    /**
     * 生成指定位数的随机数
     *
     * @param digit 位数
     * @param seed  随机数范围
     * @return 随机数，不足位数前面补0
     */
    public static String generateRandomNumber(int digit, int seed) {
        return StringUtils.complemented(digit, random(seed));
    }
}
