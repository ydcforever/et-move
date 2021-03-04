package com.fate.pool.normal;

import com.fate.pool.PoolHandler;

import java.util.List;

/**
 * @author ydc
 * @date 2020/12/28.
 */
public class NormalPool<T> extends AbstractNormalPool<T> {

    public NormalPool(int maxPoolSize, PoolHandler<T> poolHandler) {
        super(maxPoolSize, poolHandler);
    }

    public void beforeAppend() throws Exception{
        if (pool.size() == maxPoolSize) {
            handle();
        }
    }

    public void beforeAppend(List<T> list) throws Exception{
        if (pool.size() + list.size() >= maxPoolSize) {
            handle();
        }
    }

    @Override
    public void finalHandle() throws Exception {
        handle();
    }

    private void handle() throws Exception{
        try{
            if(single) {
                singleHandle();
            } else {
                poolHandler.handle(pool);
            }
        } finally {
            pool.clear();
        }
    }
}
