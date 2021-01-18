package com.airline.account.utils;

import com.airline.account.mapper.acca.SalMapper;
import com.airline.account.mapper.et.MoveLogMapper;
import com.airline.account.model.acca.Sal;
import com.airline.account.model.et.*;
import com.airline.account.service.move.ExchangeUseService;
import com.airline.account.service.move.InsertService;
import com.airline.account.service.move.MoveService;
import com.airline.account.service.move.StatusService;
import com.fate.piece.PageHandler;
import com.fate.piece.PagePiece;
import com.fate.pool.normal.CascadeSingle;
import com.fate.pool.normal.CascadeSingleFactory;
import com.fate.pool.normal.NormalPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.airline.account.utils.MatchUtil.*;

/**
 * @author ydc
 * @date 2021/1/15.
 */
@Component
public class SingleMoveComponent {

    @Autowired
    private InsertService insertService;

    @Autowired
    private MoveLogMapper moveLogMapper;

    @Autowired
    private ExchangeUseService exchangeUseService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private SalMapper salMapper;

    /**
     * 用于批量更新 E/R/U/I 状态
     *
     * @param batchSize 批处理大小
     * @param poolName  对象池key
     * @return 对象池
     */
    public NormalPool<Relation> getRelationPool(Integer batchSize, String poolName) {
        return new NormalPool<>(batchSize, list -> {
            try {
                statusService.updateSegmentStatus(list);
            } catch (Exception e) {
                Relation relation = list.get(0);
                String tktn = relation.getOperateDocumentCarrierIataNo() + relation.getOperateDocumentNo();
                MoveLog log = new MoveLog(poolName, tktn, e.getMessage());
                moveLogMapper.insertLog(log);
            }
        });
    }

    /**
     * 只有国际改签关系
     * !!! 国内改签请修改MoveService实现
     *
     * @return 票相关对象池
     */
    public CascadeSingleFactory getSalPoolFactory() {
        CascadeSingleFactory poolFactory = new CascadeSingleFactory();
        CascadeSingle<Ticket> ticketPool = new CascadeSingle<>(ticket -> insertService.insertTicketWithUpdate(ticket));
        poolFactory.addPool(POOL_KEY_TICKET, ticketPool);
        CascadeSingle<Segment> segmentPool = new CascadeSingle<>(segment -> insertService.insertSegmentWithUpdate(segment));
        poolFactory.addPool(POOL_KEY_SEGMENT, segmentPool);
        CascadeSingle<Tax> taxPool = new CascadeSingle<>(tax -> insertService.insertTaxWithUpdate(tax));
        poolFactory.addPool(POOL_KEY_TAX, taxPool);
        CascadeSingle<Relation> exchangePool = new CascadeSingle<>(relation -> {
            statusService.updateSegmentStatus(relation);
        });
        poolFactory.addPool(POOL_KEY_EXCHANGE, exchangePool);
        return poolFactory;
    }

    public PageHandler createSalPageHandler(CascadeSingleFactory poolFactory, AllocateSource allocateSource, MoveService moveService) {
        return new PageHandler() {
            @Override
            public Integer count() {
                return salMapper.countPrimaryByAllocate(allocateSource);
            }

            @Override
            public void callback(PagePiece pagePiece) {
                List<Sal> salList = salMapper.queryPrimaryByAllocate(allocateSource);
                for (Sal sal : salList) {
                    move(poolFactory, sal, moveService, allocateSource.getFileName());
                }
            }
        };
    }

    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void move(CascadeSingleFactory poolFactory, Sal primarySal, MoveService moveService, String sourceName) {
        List<Sal> salList = moveService.getSal(primarySal);
        String cnjTktString = getCnjTktString(salList);
        for (Sal s : salList) {
            Ticket ticket = getTicket(s, cnjTktString, sourceName);
            poolFactory.appendObject(POOL_KEY_TICKET, ticket);
            addSeg(poolFactory, s);
        }
        List<Tax> taxes = moveService.getTax(primarySal);
        poolFactory.appendObject(POOL_KEY_TAX, taxes);
        if (STATUS_EXCHANGE.equals(primarySal.getSaleType())) {
            //改签
            List<Relation> exchanges = moveService.getExchange(primarySal);
            for (Relation exchange : exchanges) {
                //改签关系插入
                exchangeUseService.insertExchangeWithUpdate(ERROR_EXCHANGE2ET, exchange);
                String[] status = exchange.getCouponUseIndicator().split("");
                for (String cpnNo : status) {
                    if (!COUPON_INVALID.equals(cpnNo)) {
                        Relation relation = new Relation(exchange.getOperateDocumentCarrierIataNo(), exchange.getOperateDocumentNo(),
                                EtFormat.intFormat(cpnNo), STATUS_EXCHANGE);
                        poolFactory.appendObject(POOL_KEY_EXCHANGE, relation);
                    }
                }
            }
        }

        try {
            poolFactory.finalHandle();
        } catch (Exception e) {
            String tktn = primarySal.getAirline3code() + primarySal.getFirstTicketNo();
            MoveLog log = new MoveLog(ERROR_SAL, tktn, e.getMessage());
            moveLogMapper.insertLog(log);
        }
    }

    private static void addSeg(CascadeSingleFactory factory, Sal sal) {
        if (isNumber(sal.getCouponUseIndicator())) {
            addNumberSeg(factory, sal);
        } else {
            addCharSeg(factory, sal);
        }
    }

    /**
     * 国内航段映射
     */
    private static void addCharSeg(CascadeSingleFactory factory, Sal sal) {
        String[] status = sal.getCouponUseIndicator().split("");
        for (int i = 0, len = status.length; i < len; i++) {
            if (!STATUS_VOID.equals(status[i])) {
                if (i == 0) {
                    Segment seg = buildSeg1(sal, status[i]);
                    factory.appendObject(POOL_KEY_SEGMENT, seg);
                } else if (i == 1) {
                    Segment seg = buildSeg2(sal, status[i]);
                    factory.appendObject(POOL_KEY_SEGMENT, seg);
                } else if (i == 2) {
                    Segment seg = buildSeg3(sal, status[i]);
                    factory.appendObject(POOL_KEY_SEGMENT, seg);
                } else if (i == 3) {
                    Segment seg = buildSeg4(sal, status[i]);
                    factory.appendObject(POOL_KEY_SEGMENT, seg);
                }
            }
        }
    }

    private static void addNumberSeg(CascadeSingleFactory factory, Sal sal) {
        String[] status = sal.getCouponUseIndicator().split("");
        for (String s : status) {
            if ("1".equals(s)) {
                Segment seg = buildSeg1(sal, STATUS_VALID);
                factory.appendObject(POOL_KEY_SEGMENT, seg);
            } else if ("2".equals(s)) {
                Segment seg = buildSeg2(sal, STATUS_VALID);
                factory.appendObject(POOL_KEY_SEGMENT, seg);
            } else if ("3".equals(s)) {
                Segment seg = buildSeg3(sal, STATUS_VALID);
                factory.appendObject(POOL_KEY_SEGMENT, seg);
            } else if ("4".equals(s)) {
                Segment seg = buildSeg4(sal, STATUS_VALID);
                factory.appendObject(POOL_KEY_SEGMENT, seg);
            }
        }
    }

    private static boolean isNumber(String str) {
        return str.matches(".*[0-9]+.*");
    }

}
