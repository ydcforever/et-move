package com.airline.account.mapper.acca;

import com.airline.account.model.acca.RefDp;
import com.airline.account.utils.AllocateSource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ydc
 * @date 2021/1/5.
 */
@Repository
public interface RefDpMapper {
    /**
     * 查询ACCA国内退票
     *
     * @param allocateSource  资源分配
     * @return list of ref dp
     */
    List<RefDp> queryRefDpByAllocate(AllocateSource allocateSource);

    /**
     * 国内退票资源分配总数
     *
     * @param allocateSource
     * @return total of current day in this ref dp file
     */
    Integer countRefDpByAllocate(AllocateSource allocateSource);

}
