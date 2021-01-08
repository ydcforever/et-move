package com.airline.account.service.acca;

import com.airline.account.model.acca.Relation;
import com.airline.account.model.acca.Sal;
import com.airline.account.model.et.Exchange;
import com.airline.account.utils.AllocateSource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author ydc
 * @date 2020/12/18
 */
@Service
public interface RelationService {

    /**
     * 查询改签关系
     *
     * @param primarySal 主票
     * @return list of exchange relation
     */
    List<Exchange> queryExchange(Sal primarySal);

    /**
     * 分页查询改签关系
     *
     * @param allocateSource 资源分配
     * @return list of relation
     */
    List<Exchange> queryExchangeByAllocate(AllocateSource allocateSource);

    /**
     * 改签关系分页总数
     *
     * @param allocateSource 资源分配
     * @return total of current day in this exchange file
     */
    Integer countExchangeByAllocate(AllocateSource allocateSource);

    /**
     * 分页查询退票关系
     *
     * @param allocateSource 资源分配
     * @return list of refund relation
     */
    List<Relation> queryRefundByAllocate(AllocateSource allocateSource);

    /**
     * 承运资源分页总数
     *
     * @param allocateSource 资源分配
     * @return total of current day in this refund file
     */
    Integer countRefundByAllocate(AllocateSource allocateSource);
}

