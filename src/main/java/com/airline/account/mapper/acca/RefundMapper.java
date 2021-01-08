package com.airline.account.mapper.acca;

import com.airline.account.model.acca.Relation;
import com.airline.account.utils.AllocateSource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ydc
 * @date 2021/1/8.
 */
@Repository
public interface RefundMapper {

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

}
