package com.airline.account.mapper.acca;

import com.airline.account.model.acca.*;
import com.airline.account.utils.AllocateSource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author ydc
 * @date 2020/12/18
 */
@Repository
public interface RelationMapper {

    /**
     * 查询改签关系
     *
     * @param primarySal 主票
     * @return list of exchange relation
     */
    List<Relation> queryExchange(Sal primarySal);

    /**
     * 查询退票关系
     *
     * @param allocateSource 资源分配
     * @return list of refund relation
     */
    List<Relation> queryRefundByAllocate(AllocateSource allocateSource);

    /**
     * 承运资源分配总数
     *
     * @param allocateSource 资源分配
     * @return total of current day in this refund file
     */
    Integer countRefundByAllocate(AllocateSource allocateSource);

    /**
     * 查询承运数据
     *
     * @param allocateSource 资源分配
     * @return list of upl
     */
    List<AUpl> queryUplByAllocate(AllocateSource allocateSource);

    /**
     * 承运资源分配总数
     *
     * @param allocateSource 资源分配
     * @return total of current day in this upl file
     */
    Integer countUplByAllocate(AllocateSource allocateSource);




    /**
     * 查询ACCA国内改签
     *
     * @param allocateSource  资源分配
     * @return list of rfd dp
     */
    List<RfdDp> queryRfdDpByAllocate(AllocateSource allocateSource);

    /**
     * 国内改签资源分配总数
     *
     * @param allocateSource
     * @return total of current day in this rfd dp file
     */
    Integer countRfdDpByAllocate(AllocateSource allocateSource);
}
