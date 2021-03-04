package com.airline.account.service.move;

import com.airline.account.model.et.Relation;
import org.springframework.stereotype.Service;

/**
 * @author ydc
 * @date 2021/1/15.
 */
@Service
public interface ExchangeUseService {

    /**
     * 插入改签关系
     *
     * @param logGroup 日志组
     * @param exchange 改签关系
     * @return boolean
     */
    boolean insertExchangeWithUpdate(String logGroup, Relation exchange);
}
