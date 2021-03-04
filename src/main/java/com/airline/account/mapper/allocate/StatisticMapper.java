package com.airline.account.mapper.allocate;

import com.airline.account.model.allocate.AllocateSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author ydc
 * @date 2021/2/25.
 */
@Repository
public interface StatisticMapper {
    /**
     * 统计资源
     *
     * @param tableName
     * @param fileName
     * @param dateCol
     */
    void statistic(@Param("tableName")String tableName, @Param("fileName")String fileName, @Param("dateCol")String dateCol);

    /**
     * 分片执行开始
     *
     * @param allocateSource
     */
    void beginPiece(AllocateSource allocateSource);

    /**
     * 分片执行结束
     *
     * @param allocateSource
     */
    void finishPiece(AllocateSource allocateSource);
}
