package com.fate.pool;

import javax.security.auth.Destroyable;

/**
 * @author ydc
 * @date 2020/12/26.
 */
public interface PoolFinalHandler extends Destroyable {

    /**
     * all pool must handle the rest of pool
     *
     * @throws Exception message
     */
    void finalHandle() throws Exception;

}
