package com.airline.account.service.acca;

import com.airline.account.mapper.acca.RefundMapper;
import com.airline.account.model.acca.Refund;
import com.airline.account.model.et.Relation;
import com.airline.account.service.move.RefundUseService;
import com.airline.account.utils.AllocateSource;
import com.fate.piece.PageHandler;
import com.fate.piece.PagePiece;
import com.fate.pool.normal.NormalPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.airline.account.utils.EtFormat.dateFormat;
import static com.airline.account.utils.EtFormat.intFormat;

/**
 * @author ydc
 * @date 2021/1/8.
 */
@Service
public class RefundServiceImpl implements RefundService {

    @Autowired
    private RefundMapper refundMapper;

    @Autowired
    private RefundUseService refundUseService;

    @Override
    public PageHandler createPageHandler(NormalPool<Relation> pool, AllocateSource allocateSource) {
        return new PageHandler() {
            @Override
            public Integer count() {
                return refundMapper.countRefundByAllocate(allocateSource);
            }

            @Override
            public void callback(PagePiece pagePiece) {
                List<Refund> refunds = refundMapper.queryRefundByAllocate(allocateSource);

                for (Refund refund : refunds) {
                    String[] tkts = refund.getTicketNoStr().split(";");
                    String[] tktStatus = refund.getCouponStatus().split(";");
                    for (int i = 0, len = tktStatus.length; i < len; i++) {
                        String[] cpns = tktStatus[i].split("");
                        String tkt = tkts[i];
//                        退票插入
                        Relation relation = new Relation();
                        relation.setOperateDocumentCarrierIataNo(tkt.substring(0, 3));
                        relation.setOperateDocumentNo(tkt.substring(3));
                        relation.setOperateIssueDate(dateFormat(refund.getIssueDate(), "yyyy-MM-dd"));
                        relation.setCouponUseIndicator(tktStatus[i]);
                        refundUseService.insertRefundWithUpdate(ERROR_REFUND2ET, relation);

                        for (String cpn : cpns) {
                            if (!COUPON_INVALID.equals(cpn)) {
                                try {
                                    pool.beforeAppend();
                                    Relation rel= new Relation(relation.getOperateDocumentCarrierIataNo(), relation.getOperateDocumentNo(), intFormat(cpn), STATUS_REFUND);
                                    rel.setOperateIssueDate(relation.getOperateIssueDate());
                                    pool.appendObject(rel);
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
