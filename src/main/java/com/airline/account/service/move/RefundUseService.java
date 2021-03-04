package com.airline.account.service.move;

import com.airline.account.model.et.Relation;
import org.springframework.stereotype.Service;

/**
 * @author ydc
 * @date 2021/1/15.
 */
@Service
public interface RefundUseService {

    /**
     * 退票关系
     *
     * @param logGroup 日志标识
     * @param refund 退票
     * @return boolean
     */
    boolean insertRefundWithUpdate(String logGroup, Relation refund);
}
