package com.airline.account.service.move;

import com.airline.account.mapper.acca.SalMapper;
import com.airline.account.mapper.acca.TaxDiMapper;
import com.airline.account.model.acca.Sal;
import com.airline.account.model.acca.TaxDp;
import com.airline.account.model.et.Exchange;
import com.airline.account.model.et.Tax;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.airline.account.utils.MatchUtil.getDpTax;

/**
 * @author ydc
 * @date 2020/12/29.
 */
@Service("moveDdpService")
public class MoveServiceDdpImpl implements MoveService {

    @Autowired
    private TaxDiMapper taxDiMapper;

    @Autowired
    private SalMapper salMapper;

    @Override
    public List<Sal> getSal(Sal primarySal) {
        return salMapper.queryDdpSal(primarySal);
    }

    @Override
    public List<Tax> getTax(Sal primarySal) {
        List<TaxDp> taxDps = taxDiMapper.queryDdpTax(primarySal);
        return getDpTax(taxDps, primarySal.getIssueDate());
    }

    @Override
    public List<Exchange> getExchange(Sal primarySal) {
        return null;
    }
}
