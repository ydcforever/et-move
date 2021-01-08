package com.airline.account.mapper.acca;

import com.airline.account.model.acca.Upl;
import com.airline.account.utils.AllocateSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ydc on 2020/12/2.
 */
@Repository
public interface UplMapper {

    List<Upl> queryDDpUpl(@Param("cxr") String cxr, @Param("doc") String doc, @Param("issue") String issueDate);

    List<Upl> queryDIpUpl(@Param("cxr") String cxr, @Param("doc") String doc, @Param("issue") String issueDate);

    List<Upl> queryMDpUpl(@Param("cxr") String cxr, @Param("doc") String doc, @Param("issue") String issueDate);

    List<Upl> queryMIpUpl(@Param("cxr") String cxr, @Param("doc") String doc, @Param("issue") String issueDate);

    /**
     * 分页查询承运数据
     *
     * @param allocateSource 资源分配
     * @return list of upl
     */
    List<Upl> queryUplByAllocate(AllocateSource allocateSource);

    /**
     * 承运资源分页总数
     *
     * @param allocateSource 资源分配
     * @return total of current day in this upl file
     */
    Integer countUplByAllocate(AllocateSource allocateSource);

}
