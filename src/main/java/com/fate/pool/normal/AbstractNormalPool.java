package com.fate.pool.normal;

import com.fate.pool.PoolFinalHandler;
import com.fate.pool.PoolHandler;

import javax.security.auth.Destroyable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ydc
 * @date 2020/12/24.
 */
public abstract class AbstractNormalPool<T> implements PoolFinalHandler, Destroyable {

    /**
     * storage object
     */
    protected List<T> pool;

    /**
     * max size of pool
     */
    protected int maxPoolSize;

    /**
     * solve pool
     */
    protected final PoolHandler<T> poolHandler;

    public AbstractNormalPool(int maxPoolSize, PoolHandler<T> poolHandler) {
        this.maxPoolSize = maxPoolSize;
        this.poolHandler = poolHandler;
        this.pool = new LinkedList<>();
    }

    public void appendObject(List<T> list){
        pool.addAll(list);
    }

    public void appendObject(T t) {
        pool.add(t);
    }

    @Override
    public void destroy() {
        pool.clear();
        pool = null;
    }
}
