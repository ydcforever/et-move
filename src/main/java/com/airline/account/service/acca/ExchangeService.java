package com.airline.account.service.acca;

import com.airline.account.model.acca.Sal;
import com.airline.account.model.et.Exchange;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ydc
 * @date 2021/1/8.
 */
@Service
public interface ExchangeService extends StatusPageHandler{
    /**
     * 查询改签关系
     *
     * @param primarySal 主票
     * @return list of exchange relation
     */
    List<Exchange> queryExchange(Sal primarySal);
}
