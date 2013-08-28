package com.opensoft.common;

/**
 * Description : 启动，停止接口，所有组件都实现该接口，便于统一的加载和停止
 * User : 康维
 */
public interface StartAble {
    /**
     * 启动方法
     */
    public void start();

    /**
     * 停止方法
     */
    public void stop();
}
