package com.airline.account.service.move;

import com.airline.account.model.acca.Sal;
import com.airline.account.model.et.Relation;
import com.airline.account.model.et.Tax;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.airline.account.utils.EtFormat.dateFormat;

/**
 *
 * @author ydc
 * @date 2020/9/17
 */
@Service
public interface MoveService {

    /**
     * 连票
     *
     * @param primarySal 主票
     * @return 票面信息
     */
    List<Sal> getSal(Sal primarySal);

    /**
     * 查询票面税费明细
     *
     * @param primarySal 主票
     * @return 税费明细
     */
    List<Tax> getTax(Sal primarySal);

    /**
     * 查询改签关系
     * 国内使用票面
     *
     * @param primarySal 主票
     * @return 改签关系
     */
    default List<Relation> getExchange(Sal primarySal) {
        Relation relation = new Relation();
        relation.setDocumentCarrierIataNo(primarySal.getAirline3code());
        relation.setDocumentNo(primarySal.getTicketNo());
        relation.setIssueDate(dateFormat(primarySal.getIssueDate(),"yyyy-MM-dd"));
        relation.setOperateDocumentCarrierIataNo(primarySal.getExchOriginAirline());
        relation.setOperateDocumentNo(primarySal.getExchOriginTicketNo());
        //需要查询
        relation.setIssueDate("3000-12-30");
        String status = primarySal.getExchOriginCouponNo();
        if(StringUtils.isBlank(status)) {
            status = "1234";
        }
        relation.setCouponUseIndicator(status);
        List<Relation> relations = new ArrayList<>();
        relations.add(relation);
        return relations;
    }
}
