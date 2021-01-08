package com.airline.account.service.acca;

import com.airline.account.mapper.acca.ExchangeMapper;
import com.airline.account.model.acca.Sal;
import com.airline.account.model.et.CouponStatus;
import com.airline.account.model.et.Exchange;
import com.airline.account.utils.AllocateSource;
import com.airline.account.utils.EtFormat;
import com.fate.piece.PageHandler;
import com.fate.piece.PagePiece;
import com.fate.pool.normal.NormalPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ydc
 * @date 2021/1/8.
 */
@Service
public class ExchangeServiceImpl implements ExchangeService {

    @Autowired
    private ExchangeMapper exchangeMapper;

    @Override
    public List<Exchange> queryExchange(Sal primarySal) {
        return exchangeMapper.queryExchange(primarySal);
    }

    @Override
    public PageHandler createPageHandler(NormalPool<CouponStatus> pool, AllocateSource allocateSource) {
        return new PageHandler() {
            @Override
            public Integer count() {
                return exchangeMapper.countExchangeByAllocate(allocateSource);
            }

            @Override
            public void callback(PagePiece pagePiece) {
                List<Exchange> exchanges = exchangeMapper.queryExchangeByAllocate(allocateSource);
                for (Exchange exchange : exchanges) {
                    String[] status = exchange.getExchangeCouponUseIndicator().split("");
                    for (String cpnNo : status) {
                        if (!COUPON_INVALID.equals(cpnNo)) {
                            try {
                                pool.beforeAppend();
                                CouponStatus couponStatus = new CouponStatus(exchange.getIssueInExchCarrierIataNo(),
                                        exchange.getIssueInExchDocumentNo(), EtFormat.intFormat(cpnNo), STATUS_EXCHANGE);
                                pool.appendObject(couponStatus);
                            } catch (Exception ignore) {

                            }
                        }
                    }
                }
            }
        };
    }
}
