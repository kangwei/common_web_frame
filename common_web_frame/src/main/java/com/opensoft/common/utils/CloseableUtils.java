package com.opensoft.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 关闭 Closeable 对象
 *
 * @author KangWei
 */
public class CloseableUtils {
    private static final Logger log = LoggerFactory.getLogger(CloseableUtils.class);

    /**
     * 关闭一个或多个 Closeable 对象
     *
     * @param closeables 要关闭的 Closeable 对象
     */
    public static void close(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            close0(closeable);
        }
    }

    /**
     * 停止线程池服务
     *
     * @param executorService 要停止的线程池服务
     */
    public static void shutdown(ExecutorService... executorService) {
        for (ExecutorService service : executorService) {
            if (service != null) {
                service.shutdown();
                try {
                    service.awaitTermination(6000, TimeUnit.MILLISECONDS);
                    service.shutdownNow();
                } catch (Exception e) {
                    log.error("关闭线程池{}发生异常，原因{}", service, e.getMessage());
                }
                if (!service.isShutdown()) {
                    service.shutdownNow();
                }
                log.info("关闭线程池：{} OK...", service);
                service = null;
            }
        }
    }

    private static void close0(Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();  //关闭数据流
        } catch (IOException e) {
            log.error("{}关闭异常{}", closeable.toString(), e.getMessage());
        }
    }
}
