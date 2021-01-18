package com.fate.pool.normal;

import com.fate.pool.PoolFinalHandler;
import com.fate.pool.SingleHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ydc
 * @date 2021/1/15.
 */
public class CascadeSingle<T> implements PoolFinalHandler {

    private final List<T> list;

    private final SingleHandler<T> singleHandler;

    public CascadeSingle(SingleHandler<T> singleHandler) {
        this.list = new ArrayList<>();
        this.singleHandler = singleHandler;
    }

    public void appendObject(T t){
        list.add(t);
    }

    public void appendObject(List<T> t){
        list.addAll(t);
    }

    @Override
    public void finalHandle() throws Exception {
        try{
            for(T t : list){
                singleHandler.handle(t);
            }
        } finally {
            list.clear();
        }
    }
}
