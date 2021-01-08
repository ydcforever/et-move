package com.airline.account.service.acca;

import com.airline.account.mapper.acca.RefundMapper;
import com.airline.account.model.acca.Relation;
import com.airline.account.model.et.CouponStatus;
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
public class RefundServiceImpl implements RefundService {

    @Autowired
    private RefundMapper refundMapper;

    @Override
    public PageHandler createPageHandler(NormalPool<CouponStatus> pool, AllocateSource allocateSource) {
        return new PageHandler() {
            @Override
            public Integer count() {
                return refundMapper.countRefundByAllocate(allocateSource);
            }

            @Override
            public void callback(PagePiece pagePiece) {
                List<Relation> relations = refundMapper.queryRefundByAllocate(allocateSource);

                for (Relation relation : relations) {
                    String[] tkts = relation.getTicketNoStr().split(";");
                    String[] tktStatus = relation.getCouponStatus().split(";");
                    for (int i = 0, len = tktStatus.length; i < len; i++) {
                        String[] cpns = tktStatus[i].split("");
                        String tkt = tkts[i];
//                        退票插入
//                        String[] ref = new String[]{tkt.substring(0, 3), tkt.substring(3), relation.getIssueDate(), tktStatus[i]};
//                        List<String[]> refunds = new ArrayList<>();
//                        refunds.add(ref);
//                        try {
//                            batchService.insertRefund(refunds);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        } finally {
//                            refunds.clear();
//                        }

                        for (String cpn : cpns) {
                            if (!COUPON_INVALID.equals(cpn)) {
                                try {
                                    pool.beforeAppend();
                                    CouponStatus status = new CouponStatus(tkt.substring(0, 3), tkt.substring(3), EtFormat.intFormat(cpn), STATUS_REFUND);
                                    pool.appendObject(status);
                                } catch (Exception ignore) {

                                }
                            }
                        }
                    }
                }
            }
        };
    }
}
