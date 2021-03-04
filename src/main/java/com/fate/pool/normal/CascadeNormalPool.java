package com.fate.pool.normal;

import com.fate.pool.PoolHandler;

/**
 * @author ydc
 * @date 2020/12/28.
 */
public class CascadeNormalPool<T> extends AbstractNormalPool<T> {

    public CascadeNormalPool(int maxPoolSize, PoolHandler<T> poolHandler) {
        super(maxPoolSize, poolHandler);
    }

    @Override
    public void finalHandle() throws Exception {
        if (pool.size() > 0) {
            try {
                if(single) {
                    singleHandle();
                } else {
                    for (int i = 0, len = pool.size(); i < len; i += maxPoolSize) {
                        poolHandler.handle(pool.subList(i, Math.min(i + maxPoolSize, len)));
                    }
                }
            } finally {
                pool.clear();
            }
        }
    }
}
