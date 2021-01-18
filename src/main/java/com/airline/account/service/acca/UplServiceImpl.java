package com.airline.account.service.acca;

import com.airline.account.mapper.acca.UplMapper;
import com.airline.account.model.acca.Upl;
import com.airline.account.model.et.EtUpl;
import com.airline.account.model.et.Relation;
import com.airline.account.service.move.EtUplService;
import com.airline.account.utils.AllocateSource;
import com.airline.account.utils.MatchUtil;
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

    @Autowired
    private EtUplService etUplService;

    @Override
    public PageHandler createPageHandler(NormalPool<Relation> pool, AllocateSource allocateSource) {
        return new PageHandler() {
            @Override
            public Integer count() {
                return uplMapper.countUplByAllocate(allocateSource);
            }

            @Override
            public void callback(PagePiece pagePiece) {
                List<Upl> relations = uplMapper.queryUplByAllocate(allocateSource);
                for (Upl upl : relations) {
                    //插入Et Upl
                    EtUpl etUpl = MatchUtil.getUpl(upl);
                    etUplService.insertEtUplWithUpdate(ERROR_UPL2ET, etUpl);

                    try {
                        pool.beforeAppend();
                        Relation relation = new Relation(upl.getAirline3code(), upl.getTicketNo(), upl.getCouponNo(), STATUS_FLOWN);
                        pool.appendObject(relation);
                    } catch (Exception ignore) {

                    }
                }
            }
        };
    }
}
