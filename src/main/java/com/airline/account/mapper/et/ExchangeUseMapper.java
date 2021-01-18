package com.airline.account.mapper.et;

import com.airline.account.model.et.Relation;
import org.springframework.stereotype.Repository;

/**
 * @author ydc
 * @date 2021/1/18.
 */
@Repository
public interface ExchangeUseMapper {

    /**
     * 插入改签关系
     *
     * @param exchange 改签关系
     */
    void insertExchangeUse(Relation exchange);

    /**
     * 更新改签关系
     *
     * @param exchange 改签关系
     */
    void updateExchangeUse(Relation exchange);
}
