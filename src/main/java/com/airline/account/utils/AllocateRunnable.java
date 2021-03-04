package com.airline.account.utils;

import com.airline.account.model.allocate.AllocateSource;
import com.airline.account.service.allocate.ThreadLogService;
import com.fate.piece.PageHandler;
import com.fate.pool.PoolFinalHandler;

import javax.security.auth.DestroyFailedException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author ydc
 * @date 2021/2/22.
 */
public class AllocateRunnable implements Runnable{

    private final LinkedBlockingQueue<AllocateSource> queue;

    private final String id;

    private final PoolFinalHandler poolFactory;

    private final Handler handler;

    private final ThreadLogService threadLogService;

    public AllocateRunnable(LinkedBlockingQueue<AllocateSource> queue, String id,
                            ThreadLogService threadLogService,
                            PoolFinalHandler poolFactory, Handler handler) {
        this.queue = queue;
        this.id = id;
        this.poolFactory = poolFactory;
        this.handler = handler;
        this.threadLogService = threadLogService;
    }

    @Override
    public void run() {
        while (queue.size() > 0){
            AllocateSource allocateSource = queue.poll();
            allocateSource.beginPiece(id);
            threadLogService.insertLog(allocateSource);
            PageHandler pageHandler = handler.generate(allocateSource);
            allocateSource.allPageHandle(pageHandler);
            try {
                poolFactory.finalHandle();
                allocateSource.finishPiece("Y");
            } catch (Exception e) {
                allocateSource.finishPiece("E", e.getMessage());
                e.printStackTrace();
            } finally {
                threadLogService.updateLog(allocateSource);
            }
        }
        try {
            poolFactory.destroy();
        } catch (DestroyFailedException e) {
            e.printStackTrace();
        }
    }
}
