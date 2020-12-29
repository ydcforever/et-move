package com.airline.account.mapper.acca;

import com.airline.account.model.acca.Sal;
import com.airline.account.utils.AllocateSource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author ydc
 * @date 2020/9/17
 */
@Repository
public interface SalMapper {

    /**
     * 国际日数据 连票
     *
     * @param primarySal 主票
     * @return list of sale
     */
    List<Sal> queryDipSal(Sal primarySal);

    /**
     * 国内日数据 连票
     *
     * @param primarySal 主票
     * @return list of sale
     */
    List<Sal> queryDdpSal(Sal primarySal);

    /**
     * 国际月数据 连票
     *
     * @param primarySal 主票
     * @return list of sale
     */
    List<Sal> queryMipSal(Sal primarySal);

    /**
     * 国内月数据 连票
     *
     * @param primarySal 主票
     * @return list of sale
     */
    List<Sal> queryMdpSal(Sal primarySal);

    /**
     * 文件issue date 主票总数
     *
     * @param allocateSource 资源分配
     * @return total of primary sal in the date of file
     */
    Integer countPrimaryByAllocate(AllocateSource allocateSource);

    /**
     * 文件issue date 分页主票
     *
     * @param allocateSource 资源分配
     * @return list of primary sal
     */
    List<Sal> queryPrimaryByAllocate(AllocateSource allocateSource);
}
