package com.airline.account.service.move;

import com.airline.account.mapper.acca.ExchangeMapper;
import com.airline.account.mapper.acca.SalMapper;
import com.airline.account.mapper.acca.TaxDiMapper;
import com.airline.account.model.acca.Sal;
import com.airline.account.model.acca.TaxDp;
import com.airline.account.model.et.Relation;
import com.airline.account.model.et.Tax;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.airline.account.utils.EtFormat.dateFormat;
import static com.airline.account.utils.MatchUtil.getDpTax;

/**
 *
 * @author ydc
 * @date 2020/9/17
 */
@Service("moveMdpService")
public class MoveServiceMdpImpl implements MoveService {

    @Autowired
    private TaxDiMapper taxDiMapper;

    @Autowired
    private SalMapper salMapper;

    @Autowired
    private ExchangeMapper exchangeMapper;

    @Override
    public List<Sal> getSal(Sal primarySal) {
        return salMapper.queryMdpSal(primarySal);
    }

    @Override
    public List<Tax> getTax(Sal primarySal) {
        List<TaxDp> taxDps = taxDiMapper.queryMdpTax(primarySal);
        return getDpTax(taxDps, primarySal.getIssueDate());
    }

    @Override
    public List<Relation> getExchange(Sal primarySal) {
        List<Relation> relations = new ArrayList<>();
        if(!StringUtils.isBlank(primarySal.getExchOriginTicketNo())) {
            Relation relation = new Relation();
            relation.setDocumentCarrierIataNo(primarySal.getAirline3code());
            relation.setDocumentNo(primarySal.getTicketNo());
            relation.setIssueDate(dateFormat(primarySal.getIssueDate(), "yyyy-MM-dd"));
            relation.setOperateDocumentCarrierIataNo(primarySal.getExchOriginAirline());
            relation.setOperateDocumentNo(primarySal.getExchOriginTicketNo());
            String originDate = "";
            try {
                originDate = exchangeMapper.getDpExchangeDate("ACCA_SAL_DP_M", primarySal.getExchOriginAirline(),
                        primarySal.getExchOriginTicketNo());
            } catch (Exception ignore) {
            }
            relation.setOperateIssueDate(dateFormat(originDate, "yyyy-MM-dd"));
            String status = primarySal.getExchOriginCouponNo();
            if (StringUtils.isBlank(status)) {
                status = "1234";
            }
            relation.setCouponUseIndicator(status);
            relations.add(relation);
        }
        return relations;
    }
}
