package com.airline.account.mapper.acca;

import com.airline.account.model.acca.Iwb;
import com.airline.account.utils.AllocateSource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ydc
 * @date 2021/1/8.
 */
@Repository
public interface IwbMapper {

    /**
     * 查询对外开账
     *
     * @param allocateSource  资源分配
     * @return list of iwb
     */
    List<Iwb> queryIwbByAllocate(AllocateSource allocateSource);

    /**
     * 对外开账分配总数
     *
     * @param allocateSource
     * @return total of current day in this iwb file
     */
    Integer countIwbByAllocate(AllocateSource allocateSource);

}
