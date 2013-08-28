/**
 * ClassName: ThreadPool
 * CopyRight: OpenSoft
 * Date: 13-6-9
 * Version: 1.0
 */
package com.opensoft.common.pool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Description : 线程池容器，存放框架中用到的线程池，便于管理
 *
 * @author : KangWei
 */
@Deprecated
public class ThreadPool {
    private static Map<String, ExecutorService> pool = new HashMap<String, ExecutorService>();

    static ExecutorService get(String name) {
        return pool.get(name);
    }

    static void put(String name, ExecutorService executorService) {
        pool.put(name, executorService);
    }
}
