package com.fate.pool.normal;

import com.fate.pool.PoolFinalHandler;
import com.fate.pool.PoolHandler;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ydc
 * @date 2020/12/24.
 */
public abstract class AbstractNormalPool<T> implements PoolFinalHandler {

    /**
     * storage object
     */
    protected List<T> pool;

    /**
     * max size of pool
     *
     */
    protected int maxPoolSize;

    /**
     * solve pool
     */
    protected final PoolHandler<T> poolHandler;

    /**
     * default open singleHandle when max size of pool equals 1
     *
     */
    protected boolean single = false;

    public AbstractNormalPool(int maxPoolSize, PoolHandler<T> poolHandler) {
        this.maxPoolSize = maxPoolSize;
        this.poolHandler = poolHandler;
        this.pool = new LinkedList<>();
        if (maxPoolSize == 1){
            this.single = true;
        }
    }

    public void closeSingleHandle(){
        this.single = false;
    }

    public void singleHandle() throws Exception{
        for(T t : pool){
            poolHandler.singleHandle(t);
        }
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
