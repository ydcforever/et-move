package com.airline.account.service.move;

import com.airline.account.mapper.et.EtUplMapper;
import com.airline.account.mapper.et.MoveLogMapper;
import com.airline.account.model.et.EtUpl;
import com.airline.account.model.et.MoveLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 *
 * @author ydc
 * @date 2020/12/2
 */
@Service
public class EtUplServiceImpl implements EtUplService {

    @Autowired
    private EtUplMapper etUplMapper;

    @Autowired
    private MoveLogMapper moveLogMapper;

    @Override
    public void insertEtUplWithUpdate(String logGroup, EtUpl etUpl) {
        try{
            etUplMapper.insertUpl(etUpl);
        } catch (DuplicateKeyException e){
            etUplMapper.updateUpl(etUpl);
        } catch (Exception e){
            String tktn = etUpl.getDocumentCarrierIataNo() + etUpl.getDocumentNo();
            MoveLog log = new MoveLog(logGroup, tktn, e.getMessage());
            moveLogMapper.insertLog(log);
        }
    }
}
