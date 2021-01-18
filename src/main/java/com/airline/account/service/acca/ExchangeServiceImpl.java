package com.airline.account.service.acca;

import com.airline.account.mapper.acca.ExchangeMapper;
import com.airline.account.model.acca.Sal;
import com.airline.account.model.et.Relation;
import com.airline.account.service.move.ExchangeUseService;
import com.airline.account.utils.AllocateSource;
import com.fate.piece.PageHandler;
import com.fate.piece.PagePiece;
import com.fate.pool.normal.NormalPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.airline.account.utils.EtFormat.intFormat;

/**
 * @author ydc
 * @date 2021/1/8.
 */
@Service
public class ExchangeServiceImpl implements ExchangeService {

    @Autowired
    private ExchangeMapper exchangeMapper;

    @Autowired
    private ExchangeUseService exchangeUseService;

    @Override
    public List<Relation> queryExchange(Sal primarySal) {
        return exchangeMapper.queryExchange(primarySal);
    }

    @Override
    public PageHandler createPageHandler(NormalPool<Relation> pool, AllocateSource allocateSource) {
        return new PageHandler() {
            @Override
            public Integer count() {
                return exchangeMapper.countExchangeByAllocate(allocateSource);
            }

            @Override
            public void callback(PagePiece pagePiece) {
                List<Relation> exchanges = exchangeMapper.queryExchangeByAllocate(allocateSource);
                for (Relation exchange : exchanges) {
                    //插入改签关系
                    exchangeUseService.insertExchangeWithUpdate(ERROR_EXCHANGE2ET, exchange);
                    String[] status = exchange.getCouponUseIndicator().split("");
                    for (String cpnNo : status) {
                        if (!COUPON_INVALID.equals(cpnNo)) {
                            try {
                                pool.beforeAppend();
                                Relation relation = new Relation(exchange.getOperateDocumentCarrierIataNo(),
                                        exchange.getOperateDocumentNo(), intFormat(cpnNo), STATUS_EXCHANGE);
                                pool.appendObject(relation);
                            } catch (Exception ignore) {

                            }
                        }
                    }
                }
            }
        };
    }
}
