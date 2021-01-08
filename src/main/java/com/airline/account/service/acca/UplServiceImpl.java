package com.airline.account.service.acca;

import com.airline.account.mapper.acca.UplMapper;
import com.airline.account.model.acca.Upl;
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
public class UplServiceImpl implements UplService {

    @Autowired
    private UplMapper uplMapper;

    @Override
    public PageHandler createPageHandler(NormalPool<CouponStatus> pool, AllocateSource allocateSource) {
        return new PageHandler() {
            @Override
            public Integer count() {
                return uplMapper.countUplByAllocate(allocateSource);
            }

            @Override
            public void callback(PagePiece pagePiece) {
                List<Upl> relations = uplMapper.queryUplByAllocate(allocateSource);
//                承运插入
//                List<Upl> upls = getUpl(relations, allocateSource.getFileName());
//                for(Upl upl : upls) {
//                    try {
//                        insertService.insertUplWithUpdate(upl);
//                    } catch (Exception e) {
//                        MoveLog log = new MoveLog(ERROR_UPL, upl.getDocumentCarrierIataNo() + upl.getDocumentNo(), e.getMessage());
//                        moveLogMapper.insertLog(log);
//                    }
//                }

                for (Upl upl : relations) {
                    try {
                        pool.beforeAppend();
                        CouponStatus status = new CouponStatus(upl.getAirline3code(), upl.getTicketNo(), upl.getCouponNo(), STATUS_FLOWN);
                        pool.appendObject(status);
                    } catch (Exception ignore) {

                    }
                }
            }
        };
    }

}
