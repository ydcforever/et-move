package com.fate.thread;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author ydc
 * @date 2021/2/22.
 */
public interface ResourceRunnable<T> {

    /**
     * generate runnable which will consume resource
     *
     * @param resource all resource which will be execute
     * @param sequence the sequence of thread
     * @return runnable
     */
    Runnable generate(LinkedBlockingQueue<T> resource, int sequence);

}
