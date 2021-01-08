package com.airline.account.service.acca;

import com.airline.account.mapper.acca.IwbMapper;
import com.airline.account.model.acca.Iwb;
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
public class IwbServiceImpl implements IwbService {

    @Autowired
    private IwbMapper iwbMapper;

    @Override
    public PageHandler createPageHandler(NormalPool<CouponStatus> pool, AllocateSource allocateSource) {
        return new PageHandler() {
            @Override
            public Integer count() {
                return iwbMapper.countIwbByAllocate(allocateSource);
            }

            @Override
            public void callback(PagePiece pagePiece) {
                List<Iwb> iwbList = iwbMapper.queryIwbByAllocate(allocateSource);
                for (Iwb iwb : iwbList) {
                    try {
                        pool.beforeAppend();
                        CouponStatus status = new CouponStatus(iwb.getAirline3code(), iwb.getTicketNo(), iwb.getCouponNo(), STATUS_IWB);
                        pool.appendObject(status);
                    } catch (Exception ignore) {

                    }
                }
            }
        };
    }
}
