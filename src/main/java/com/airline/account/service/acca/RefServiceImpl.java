package com.airline.account.service.acca;

import com.airline.account.mapper.acca.RefDpMapper;
import com.airline.account.model.acca.RefDp;
import com.airline.account.model.et.Relation;
import com.airline.account.service.move.RefundUseService;
import com.airline.account.model.allocate.AllocateSource;
import com.fate.piece.PageHandler;
import com.fate.piece.PagePiece;
import com.fate.pool.normal.NormalPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.airline.account.utils.EtFormat.dateFormat;


/**
 * @author ydc
 * @date 2021/1/8.
 */
@Service
public class RefServiceImpl implements RefService {

    @Autowired
    private RefDpMapper refDpMapper;

    @Autowired
    private RefundUseService refundUseService;

    @Override
    public PageHandler createPageHandler(NormalPool<Relation> pool, AllocateSource allocateSource) {
        return new PageHandler() {
            @Override
            public Integer count() {
                return refDpMapper.countRefDpByAllocate(allocateSource);
            }

            @Override
            public void callback(PagePiece pagePiece) {
                List<RefDp> relations = refDpMapper.queryRefDpByAllocate(allocateSource);
                for (RefDp refDp : relations) {
                    //插入ref
                    Relation relation = new Relation(refDp.getRefundOwnerCompany(), refDp.getRefundTicketNo(),
                            refDp.getRefundRelationNo(), STATUS_REFUND);
                    relation.setOperateIssueDate(dateFormat(refDp.getRefundDate(), "yyyy-MM-dd"));
                    relation.setCouponUseIndicator(refDp.getRefundRelationNo() + "");
                    //精细化切点
                    refundUseService.insertRefundWithUpdate(ERROR_REF_DP2ET, relation);

                    try {
                        pool.beforeAppend();
                        pool.appendObject(relation);
                    } catch (Exception ignore) {

                    }
                }
            }
        };
    }
}
