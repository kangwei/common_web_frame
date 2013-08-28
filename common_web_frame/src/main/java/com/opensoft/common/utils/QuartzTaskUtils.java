/**
 * QuartzTaskUtils
 * Date: 12-6-12
 * Copyright: OpenSoft
 * Version: 1.0
 */
package com.opensoft.common.utils;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Date;

/**
 * 定时执行指定的任务。任务指的是一个 Runnable 对象。
 *
 * @author KangWei
 */
public class QuartzTaskUtils {
    private static final Logger log = LoggerFactory.getLogger(QuartzTaskUtils.class);

    /**
     * 5分钟
     */
    public static final int FIVE_MINUTES = 5 * 60;

    /**
     * 10分钟
     */
    public static final int TEN_MINUTES = 10 * 60;

    /**
     * 1小时
     */
    public static final int ONE_HOUR = 60 * 60;

    private static Scheduler scheduler;

    private static Scheduler getScheduler() throws SchedulerException {
        if (scheduler == null) {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            scheduler = schedulerFactory.getScheduler();
        }

        return scheduler;
    }

    /**
     * 每天执行指定的任务
     *
     * @param hour   执行时间 - 小时部分
     * @param minute 执行时间 - 分钟部分
     * @param clazz  要执行的任务
     * @throws org.quartz.SchedulerException 异常
     */
    public static void everyDay(int hour, int minute, final Class<? extends Job> clazz) throws SchedulerException {
        JobDetail job = JobBuilder.newJob(clazz).withIdentity(clazz.getName()).build();

        Date runTime = DateBuilder.todayAt(hour, minute, 0);
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(clazz.getName())
                .startAt(runTime).withSchedule(
                SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(24).repeatForever()
        ).build();

        getScheduler().scheduleJob(job, trigger);

        getScheduler().start();
    }

    /**
     * 每天执行指定的任务
     *
     * @param conExpression 时间表达式
     * @param clazz         要执行的任务
     * @param initRun       是否立即执行
     * @throws org.quartz.SchedulerException 异常
     * @throws java.text.ParseException     异常
     */
    public static void everyDay(String conExpression, final Class<? extends Job> clazz, boolean initRun) throws SchedulerException, ParseException {
        JobDetail job = JobBuilder.newJob(clazz).withIdentity(clazz.getName()).build();

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(clazz.getName())
                .withSchedule(CronScheduleBuilder.cronSchedule(conExpression))
                    .build();

        getScheduler().scheduleJob(job, trigger);

        if (initRun) {
            getScheduler().triggerJob(new JobKey(clazz.getName()));
        }

        getScheduler().start();
    }

    /**
     * 从当前时间开始每隔指定的时间间隔执行一次指定任务
     *
     * @param period 时间间隔，毫秒为单位
     * @param clazz  要执行的任务
     * @throws org.quartz.SchedulerException 异常
     */
    public static void every(int period, final Class<? extends Job> clazz) throws SchedulerException {
        JobDetail job = JobBuilder.newJob(clazz).build();

        Trigger trigger = TriggerBuilder.newTrigger().startNow().withSchedule(
                SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(period)
                        .repeatForever()
        ).build();

        getScheduler().scheduleJob(job, trigger);

        getScheduler().start();
    }

    /**
     * 关闭定时器
     */
    public static void close() {
        try {
            log.info("关闭定时任务...");
            if (getScheduler() != null) {
                getScheduler().shutdown(true);
            }
        } catch (SchedulerException e) {
            log.error("关闭定时任务异常", e);
        }
    }
}
