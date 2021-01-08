package com.airline.account.service.acca;

import com.airline.account.mapper.acca.RefDpMapper;
import com.airline.account.model.acca.RefDp;
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
public class RefServiceImpl implements RefService {

    @Autowired
    private RefDpMapper refDpMapper;

    @Override
    public PageHandler createPageHandler(NormalPool<CouponStatus> pool, AllocateSource allocateSource) {
        return new PageHandler() {
            @Override
            public Integer count() {
                return refDpMapper.countRefDpByAllocate(allocateSource);
            }

            @Override
            public void callback(PagePiece pagePiece) {
                List<RefDp> relations = refDpMapper.queryRefDpByAllocate(allocateSource);
                for (RefDp refDp : relations) {
                    try {
                        pool.beforeAppend();
                        CouponStatus status = new CouponStatus(refDp.getRefundOwnerCompany(), refDp.getRefundTicketNo(),
                                refDp.getRefundRelationNo(), STATUS_REFUND);
                        pool.appendObject(status);
                    } catch (Exception ignore) {

                    }
                }
            }
        };
    }
}
