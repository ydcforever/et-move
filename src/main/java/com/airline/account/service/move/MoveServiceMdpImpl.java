package com.airline.account.service.move;

import com.airline.account.mapper.acca.SalMapper;
import com.airline.account.mapper.acca.TaxDiMapper;
import com.airline.account.model.acca.Relation;
import com.airline.account.model.acca.Sal;
import com.airline.account.model.acca.TaxDp;
import com.airline.account.model.et.Tax;
import com.airline.account.service.acca.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private RelationService relationService;

    @Override
    public String dataSource() {
        return "ACCA_DP_M";
    }

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
        return relationService.queryExchange(primarySal);
    }
}
