package com.airline.account.utils;

import com.airline.account.model.et.Segment;
import com.airline.account.model.et.Tax;
import com.airline.account.model.et.Ticket;
import com.fate.pool.PoolFinalHandler;
import com.fate.pool.normal.CascadeNormalPool;
import lombok.Data;

import javax.security.auth.Destroyable;

/**
 * @author ydc
 * @date 2020/12/23.
 */
@Data
public class CascadeNormalPoolFactory implements PoolFinalHandler, Destroyable {

    private final int maxPoolSize;

    private int offset = 0;

    protected CascadeNormalPool<Ticket> ticketPool;

    protected CascadeNormalPool<Segment> segmentPool;

    protected CascadeNormalPool<Tax> taxPool;

    protected CascadeNormalPool<String[]> exchangePool;

    public CascadeNormalPoolFactory(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
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
     * @throws Exception e
     */
    private void handle() throws Exception {
        try{
            ticketPool.finalHandle();
            segmentPool.finalHandle();
            exchangePool.finalHandle();
            taxPool.finalHandle();
        } finally {
            offset = 0;
        }
    }

    @Override
    public void destroy()  {
        ticketPool.destroy();
        segmentPool.destroy();
        exchangePool.destroy();
        taxPool.destroy();
    }
}
