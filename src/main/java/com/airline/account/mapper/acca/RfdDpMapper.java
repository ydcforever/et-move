package com.airline.account.mapper.acca;

import com.airline.account.model.acca.RfdDp;
import com.airline.account.model.allocate.AllocateSource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ydc
 * @date 2021/1/5.
 */
@Repository
public interface RfdDpMapper {
    /**
     * 查询ACCA国内改签
     *
     * @param allocateSource  资源分配
     * @return list of rfd dp
     */
    List<RfdDp> queryRfdDpByAllocate(AllocateSource allocateSource);

    /**
     * ACCA国内改签资源分配总数
     *
     * @param allocateSource 资源分配
     * @return total of the ref dp file
     */
    Integer countRfdDpByAllocate(AllocateSource allocateSource);
}
