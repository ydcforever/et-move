package com.fate.pool.normal;

import com.fate.pool.PoolFinalHandler;

import javax.security.auth.Destroyable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ydc
 * @date 2020/12/23.
 */
public class CascadeNormalPoolFactory implements PoolFinalHandler, Destroyable {

    private final int maxPoolSize;

    private int offset = 0;

    private Map<String, CascadeNormalPool> map;

    public CascadeNormalPoolFactory(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
        this.map = new HashMap<>(10);
    }

    /**
     * must add overflow pool
     *
     * @param key pool key
     * @param pool save object
     */
    public void addPool(String key, CascadeNormalPool pool) {
        map.put(key, pool);
    }

    public <T> void appendObject(String key, T t) {
        CascadeNormalPool pool = map.get(key);
        pool.appendObject(t);
    }

    public <T> void appendObject(String key, List<T> list) {
        CascadeNormalPool pool = map.get(key);
        pool.appendObject(list);
    }

    public void afterAllAppend() throws Exception{
        offset++;
        if(offset == maxPoolSize) {
            handle();
        }
    }

    @Override
    public void finalHandle() throws Exception{
        if(offset > 0) {
            handle();
        }
    }

    /**
     * If those pool has exception, stop handle and clear all pool.
     * You must be alive to the rest of data which will be discard.
     *
     * @throws Exception
     */
    private void handle() throws Exception {
        Collection<CascadeNormalPool> collection = map.values();
        try{
            for(CascadeNormalPool poolPiece : collection) {
                poolPiece.finalHandle();
            }
        } finally {
            offset = 0;
        }
    }

    @Override
    public void destroy()  {
        map.clear();
        map = null;
    }
}
