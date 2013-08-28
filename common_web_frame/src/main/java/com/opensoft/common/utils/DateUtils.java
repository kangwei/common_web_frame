package com.opensoft.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Description:日期工具类
 *
 * @author KangWei
 */
public class DateUtils extends org.apache.commons.lang.time.DateFormatUtils {
    private static final DateFormat DATE_FORMAT_YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 字符串To日期
     *
     * @param str    日期字符串
     * @param format 格式
     * @return 日期
     */
    public static Date formatDateFormStr(String str, String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sf.parse(str.trim());
        } catch (Exception e) {
            //nothing to do
        }
        return date;
    }

    /**
     * 格式化日期时间
     *
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date, String format) {
        if (date != null) {
            return new SimpleDateFormat(format).format(date);
        }

        return null;
    }

    /**
     * 格式化当前日期时间
     *
     * @param format
     * @return
     */
    public static String now(String format) {
        return format(new Date(), format);
    }

    /**
     * 计算指定间隔的终止时间
     *
     * @param dateStart 开始时间
     * @param interval  时间间隔
     * @param timeUnit  时间单位
     * @return 终止时间
     * @throws IllegalArgumentException 不支持的时间单位异常
     */
    public static Date addInterval(Date dateStart, int interval, TimeUnit timeUnit) throws IllegalArgumentException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateStart);

        if (TimeUnit.SECONDS.equals(timeUnit)) {
            calendar.add(Calendar.SECOND, interval);
        } else if (TimeUnit.MILLISECONDS.equals(timeUnit)) {
            calendar.add(Calendar.MILLISECOND, interval);
        } else if (TimeUnit.MINUTES.equals(timeUnit)) {
            calendar.add(Calendar.MINUTE, interval);
        } else if (TimeUnit.HOURS.equals(timeUnit)) {
            calendar.add(Calendar.HOUR, interval);
        } else if (TimeUnit.DAYS.equals(timeUnit)) {
            calendar.add(Calendar.DAY_OF_YEAR, interval);
        } else {
            throw new IllegalArgumentException("不支持的时间单位");
        }

        return calendar.getTime();
    }

    /**
     * @param @param  begin
     * @param @param  end
     * @param @return
     * @return String
     * @throws
     * @Title: countTime
     * @Description: 计算时间差
     */
    public static String countTime(Date begin, Date end) {
        StringBuilder sb = new StringBuilder();
        long total_time = end.getTime() - begin.getTime();
        long day = total_time / (24 * 60 * 60 * 1000);
        long hour = (total_time / (60 * 60 * 1000) - day * 24);
        long min = ((total_time / (60 * 1000)) - day * 24 * 60 - hour * 60);
        if (day != 0) {
            sb.append(day).append("天");
        }
        if (hour != 0) {
            sb.append(hour).append("小时");
        }
        sb.append(min).append("分钟");
        return sb.toString();
    }

    /**
     * 根据本周第几天获取日期
     *
     * @param day：Calendar.MONDAY-Calendar.SUNDAY
     *
     * @return 日期 yyyy-MM-dd
     */
    public static String getWeekDay(int day, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_WEEK, day);
        return new SimpleDateFormat(format).format(calendar.getTime());
    }


    /**
     * 返回当前日期的星期
     *
     * @param str    日期
     * @param format 格式
     * @return 星期
     */
    public static String dayForWeek(String str, String format) {
        final String dayNames[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
                "星期六"};

        Date date = formatDateFormStr(str, format);
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int day = c.get(Calendar.DAY_OF_WEEK) - 1;
        return dayNames[day];
    }

    public static String getWeek(int format) {
        switch (format) {
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            case 7:
                return "星期日";
            default:
                return "";
        }
    }

    /**
     * 时间转换为时间段，例：09：00-12：00 转换为上午
     *
     * @param str    时间
     * @param format 时间格式
     * @return 时间段
     */
    public static String timeToTimeRange(String str, String format) {
        Date date = formatDateFormStr(str, format);
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return getRange(hour, minute);
    }

    /**
     * 根据小时和分钟确定时间段
     *
     * @param hour   小时
     * @param minute 分钟
     * @return 时间段
     */
    private static String getRange(int hour, int minute) {
        if ((hour >= 0 && hour <= 4) || (hour == 5 && minute == 0)) {
            return "凌晨";
        } else if (hour >= 5 && hour <= 6) {
            return "清晨";
        } else if (hour >= 7 && hour <= 8) {
            return "早上";
        } else if ((hour >= 9 && hour <= 11) || (hour == 12 && minute == 0)) {
            return "上午";
        } else if (hour >= 12 && hour <= 13) {
            return "中午";
        } else if (hour >= 14 && hour <= 17) {
            return "下午";
        } else if (hour == 18) {
            return "傍晚";
        } else if (hour >= 19 && hour <= 23) {
            return "晚上";
        } else {
            return "";
        }
    }

}
