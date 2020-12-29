package com.fate.pool;

/**
 * @author ydc
 * @date 2020/12/26.
 */
public interface PoolFinalHandler {

    /**
     * all pool must handle the rest of pool
     *
     * @throws Exception message
     */
    void finalHandle() throws Exception;

}
