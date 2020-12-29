package com.airline.account.mapper.acca;

import com.airline.account.model.acca.AUpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ydc on 2020/12/2.
 */
@Repository
public interface AUplMapper {

    List<AUpl> queryDDpUpl(@Param("cxr") String cxr, @Param("doc") String doc, @Param("issue") String issueDate);

    List<AUpl> queryDIpUpl(@Param("cxr") String cxr, @Param("doc") String doc, @Param("issue") String issueDate);

    List<AUpl> queryMDpUpl(@Param("cxr") String cxr, @Param("doc") String doc, @Param("issue") String issueDate);

    List<AUpl> queryMIpUpl(@Param("cxr") String cxr, @Param("doc") String doc, @Param("issue") String issueDate);
}
