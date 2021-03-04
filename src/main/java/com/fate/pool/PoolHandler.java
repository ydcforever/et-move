package com.fate.pool;

import java.util.List;

/**
 * @author ydc
 * @date 2020/12/24.
 */
public interface PoolHandler<T> {

    /**
     * solve pool which overflow max pool size
     *
     * @param list pool Object
     * @throws Exception handle
     */
    default void handle(List<T> list) throws Exception {

    }

    /**
     * solve pool with more action when batch size equals 1
     * @param t Object
     * @throws Exception e
     */
    default void singleHandle(T t) throws Exception{

    }
}
