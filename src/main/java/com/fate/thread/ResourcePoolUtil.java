package com.fate.thread;

import java.util.concurrent.*;

/**
 * @author ydc
 * @date 2021/2/22.
 */
public final class ResourcePoolUtil {

    public static <T> ThreadPoolExecutor createThreadPool(LinkedBlockingQueue<T> resource,
                                                          ResourceRunnable<T> resourceRunnable,
                                                          int corePoolSize, long keepAliveTime){
        ThreadFactory factory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r);
            }
        };
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, corePoolSize,
                keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<>(corePoolSize),
                factory, new ThreadPoolExecutor.CallerRunsPolicy());
        for (int i = 0; i < corePoolSize; i++) {
            Runnable allocateRunnable = resourceRunnable.generate(resource, i);
            executor.execute(allocateRunnable);
        }
        return executor;
    }

    public static void shutdown(ThreadPoolExecutor executor, long timeout){
        try {
            executor.shutdown();
            if (!executor.awaitTermination(timeout, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}
