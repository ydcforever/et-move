package com.airline.account.service.acca;

import com.airline.account.mapper.acca.IwbMapper;
import com.airline.account.mapper.et.MoveLogMapper;
import com.airline.account.model.acca.Iwb;
import com.airline.account.model.et.EtUpl;
import com.airline.account.model.et.MoveLog;
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
public class IwbServiceImpl implements IwbService {

    @Autowired
    private IwbMapper iwbMapper;

    @Autowired
    private EtUplService etUplService;

    @Autowired
    private MoveLogMapper moveLogMapper;

    @Override
    public PageHandler createPageHandler(NormalPool<Relation> pool, AllocateSource allocateSource) {
        return new PageHandler() {
            @Override
            public Integer count() {
                return iwbMapper.countIwbByAllocate(allocateSource);
            }

            @Override
            public void callback(PagePiece pagePiece) {
                List<Iwb> iwbList = iwbMapper.queryIwbByAllocate(allocateSource);
                for (Iwb iwb : iwbList) {
                    //iwb 插入ET upl
                    EtUpl etUpl = MatchUtil.getUpl(iwb);
                    etUplService.insertEtUplWithUpdate(ERROR_IWB2UPL, etUpl);

                    try {
                        pool.beforeAppend();
                        Relation relation = new Relation(iwb.getAirline3code(), iwb.getTicketNo(), iwb.getCouponNo(), STATUS_IWB);
                        pool.appendObject(relation);
                    } catch (Exception e) {
                        MoveLog moveLog = new MoveLog(ERROR_IWB_STATUS, iwb.getAirline3code() + iwb.getTicketNo(), e.getMessage());
                        moveLogMapper.insertLog(moveLog);
                    }
                }
            }
        };
    }
}
