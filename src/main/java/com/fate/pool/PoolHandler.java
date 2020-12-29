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
    void handle(List<T> list) throws Exception;
}
