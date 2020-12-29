package com.airline.account.service.move;

import com.airline.account.mapper.acca.SalMapper;
import com.airline.account.mapper.acca.TaxDiMapper;
import com.airline.account.model.acca.Relation;
import com.airline.account.model.acca.Sal;
import com.airline.account.model.acca.TaxIp;
import com.airline.account.model.et.Tax;
import com.airline.account.service.acca.RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.airline.account.utils.MatchUtil.getIpTax;

/**
 * @author ydc
 * @date 2020/12/29.
 */
@Service("moveMipService")
public class MoveServiceMipImpl implements MoveService{

    @Autowired
    private TaxDiMapper taxDiMapper;

    @Autowired
    private SalMapper salMapper;

    @Autowired
    private RelationService relationService;

    @Override
    public String dataSource() {
        return "ACCA_IP_M";
    }

    @Override
    public List<Sal> getSal(Sal primarySal) {
        return salMapper.queryMipSal(primarySal);
    }

    @Override
    public List<Tax> getTax(Sal primarySal) {
        List<TaxIp> taxIps = taxDiMapper.queryMipTax(primarySal);
        return getIpTax(taxIps, primarySal.getIssueDate());
    }

    @Override
    public List<Relation> getExchange(Sal primarySal) {
        return relationService.queryExchange(primarySal);
    }
}
