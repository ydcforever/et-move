package com.fate.pool.normal;

import com.fate.pool.PoolFinalHandler;

import javax.security.auth.Destroyable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ydc
 * @date 2021/1/15.
 */
public class CascadeSingleFactory implements PoolFinalHandler, Destroyable {

    private Map<String, CascadeSingle> map;

    public CascadeSingleFactory() {
        this.map = new HashMap<>(10);
    }

    /**
     * must add overflow pool
     *
     * @param key pool key
     * @param pool save object
     */
    public void addPool(String key, CascadeSingle pool) {
        map.put(key, pool);
    }

    public <T> void appendObject(String key, T t) {
        CascadeSingle pool = map.get(key);
        pool.appendObject(t);
    }

    public <T> void appendObject(String key, List<T> list) {
        CascadeSingle pool = map.get(key);
        pool.appendObject(list);
    }

    @Override
    public void finalHandle() throws Exception{
        Collection<CascadeSingle> collection = map.values();
        for(CascadeSingle poolPiece : collection) {
            poolPiece.finalHandle();
        }
    }

    @Override
    public void destroy()  {
        map.clear();
        map = null;
    }
}
