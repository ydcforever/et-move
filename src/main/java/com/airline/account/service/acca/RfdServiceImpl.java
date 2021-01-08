package com.airline.account.service.acca;

import com.airline.account.mapper.acca.RfdDpMapper;
import com.airline.account.model.acca.RfdDp;
import com.airline.account.model.et.CouponStatus;
import com.airline.account.utils.AllocateSource;
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
public class RfdServiceImpl implements RfdService {

    @Autowired
    private RfdDpMapper rfdDpMapper;

    @Override
    public PageHandler createPageHandler(NormalPool<CouponStatus> pool, AllocateSource allocateSource) {
        return new PageHandler() {
            @Override
            public Integer count() {
                return rfdDpMapper.countRfdDpByAllocate(allocateSource);
            }

            @Override
            public void callback(PagePiece pagePiece) {
                List<RfdDp> relations = rfdDpMapper.queryRfdDpByAllocate(allocateSource);
                for (RfdDp rfdDp : relations) {
                    try {
                        pool.beforeAppend();
                        CouponStatus status = new CouponStatus(rfdDp.getTransferTicketCompany(), rfdDp.getTransferTicketNo(), rfdDp.getCouponNo(), STATUS_EXCHANGE);
                        pool.appendObject(status);
                    } catch (Exception ignore) {

                    }
                }
            }
        };
    }
}
