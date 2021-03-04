package com.airline.account.mapper.acca;

import com.airline.account.model.acca.Sal;
import com.airline.account.model.et.Relation;
import com.airline.account.model.allocate.AllocateSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ydc
 * @date 2021/1/8.
 */
@Repository
public interface ExchangeMapper {

    /**
     * 查询改签关系
     *
     * @param primarySal 主票
     * @return list of exchange relation
     */
    List<Relation> queryExchange(Sal primarySal);

    /**
     * 分页查询改签关系
     *
     * @param allocateSource 资源分配
     * @return list of relation
     */
    List<Relation> queryExchangeByAllocate(AllocateSource allocateSource);

    /**
     * 改签关系分页总数
     *
     * @param allocateSource 资源分配
     * @return total of current day in this exchange file
     */
    Integer countExchangeByAllocate(AllocateSource allocateSource);


    /**
     * 国内改签原票日期
     * @param tableName
     * @param airline
     * @param ticketNo
     * @return
     */
    String getDpExchangeDate(@Param("tableName") String tableName, @Param("airline")String airline, @Param("ticketNo")String ticketNo);
}
