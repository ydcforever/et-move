package com.airline.account.service.move;

import com.airline.account.mapper.acca.AUplMapper;
import com.airline.account.model.acca.AUpl;
import com.airline.account.model.et.Upl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.airline.account.utils.MatchUtil.getUpl;

/**
 *
 * @author ydc
 * @date 2020/12/2
 */
@Service
public class UplServiceImpl implements UplService {

    @Autowired
    private AUplMapper aUplMapper;

    @Override
    public List<Upl> moveDdpUpl(String airline, String ticketNo, String issueDate) {
        List<AUpl> list = aUplMapper.queryDDpUpl(airline, ticketNo, issueDate);
        return getUpl(list, "ACCA_DP_D");
    }

    @Override
    public List<Upl> moveDipUpl(String airline, String ticketNo, String issueDate) {
        List<AUpl> list = aUplMapper.queryDIpUpl(airline, ticketNo, issueDate);
        return getUpl(list, "ACCA_IP_D");
    }

    @Override
    public List<Upl> moveMdpUpl(String airline, String ticketNo, String issueDate) {
        List<AUpl> list = aUplMapper.queryMDpUpl(airline, ticketNo, issueDate);
        return getUpl(list, "ACCA_DP_M");
    }

    @Override
    public List<Upl> moveMipUpl(String airline, String ticketNo, String issueDate) {
        List<AUpl> list = aUplMapper.queryMIpUpl(airline, ticketNo, issueDate);
        return getUpl(list, "ACCA_IP_M");
    }
}
