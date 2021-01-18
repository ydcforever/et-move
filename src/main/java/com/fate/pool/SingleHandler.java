package com.fate.pool;

/**
 * @author ydc
 * @date 2021/1/15.
 */
public interface SingleHandler<T> {
    /**
     * solve pool which overflow max pool size
     *
     * @param t Object
     * @throws Exception handle
     */
    void handle(T t) throws Exception;
}
