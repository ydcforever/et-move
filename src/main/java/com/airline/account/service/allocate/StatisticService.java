package com.airline.account.service.allocate;

import com.airline.account.model.allocate.AllocateSource;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author ydc
 * @date 2021/2/25.
 */
@Service
public interface StatisticService {

    /**
     * 获取资源
     *
     * @param allocateSource
     * @return
     */
    LinkedBlockingQueue<AllocateSource> getResource(AllocateSource allocateSource);

    /**
     * 统计
     * 主要服务于手动添加任务
     *
     * @param tableName
     * @param dateCol
     */
    void statistic(String tableName, String dateCol);

    /**
     * 线程执行开始
     *
     * @param allocateSource
     */
    void beginPiece(AllocateSource allocateSource);

    /**
     * 线程执行结束
     *
     * @param allocateSource
     */
    void finishPiece(AllocateSource allocateSource);

}
