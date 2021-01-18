package com.airline.account.service.move;

import com.airline.account.mapper.et.ExchangeUseMapper;
import com.airline.account.mapper.et.MoveLogMapper;
import com.airline.account.model.et.MoveLog;
import com.airline.account.model.et.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * @author ydc
 * @date 2021/1/15.
 */
@Service
public class ExchangeUseServiceImpl implements ExchangeUseService {

    @Autowired
    private ExchangeUseMapper exchangeUseMapper;

    @Autowired
    private MoveLogMapper moveLogMapper;

    @Override
    public void insertExchangeWithUpdate(String logGroup, Relation exchange) {
        try{
            exchangeUseMapper.insertExchangeUse(exchange);
        } catch (DuplicateKeyException e) {
            exchangeUseMapper.updateExchangeUse(exchange);
        } catch (Exception e) {
            String tktn = exchange.getDocumentCarrierIataNo() + exchange.getDocumentNo();
            MoveLog moveLog = new MoveLog(logGroup, tktn, e.getMessage());
            moveLogMapper.insertLog(moveLog);
        }
    }
}
