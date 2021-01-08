package com.airline.account.service.move;

import com.airline.account.mapper.acca.UplMapper;
import com.airline.account.model.acca.Upl;
import com.airline.account.model.et.EtUpl;
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
public class EtUplServiceImpl implements EtUplService {

    @Autowired
    private UplMapper uplMapper;

    @Override
    public List<EtUpl> moveDdpUpl(String airline, String ticketNo, String issueDate) {
        List<Upl> list = uplMapper.queryDDpUpl(airline, ticketNo, issueDate);
        return getUpl(list);
    }

    @Override
    public List<EtUpl> moveDipUpl(String airline, String ticketNo, String issueDate) {
        List<Upl> list = uplMapper.queryDIpUpl(airline, ticketNo, issueDate);
        return getUpl(list);
    }

    @Override
    public List<EtUpl> moveMdpUpl(String airline, String ticketNo, String issueDate) {
        List<Upl> list = uplMapper.queryMDpUpl(airline, ticketNo, issueDate);
        return getUpl(list);
    }

    @Override
    public List<EtUpl> moveMipUpl(String airline, String ticketNo, String issueDate) {
        List<Upl> list = uplMapper.queryMIpUpl(airline, ticketNo, issueDate);
        return getUpl(list);
    }
}
