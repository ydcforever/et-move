package com.airline.account.utils;

import com.airline.account.mapper.acca.SalMapper;
import com.airline.account.mapper.et.MoveLogMapper;
import com.airline.account.model.acca.Sal;
import com.airline.account.model.et.*;
import com.airline.account.service.move.BatchService;
import com.airline.account.service.move.MoveService;
import com.fate.piece.PageHandler;
import com.fate.piece.PagePiece;
import com.fate.pool.normal.CascadeNormalPool;
import com.fate.pool.normal.CascadeNormalPoolFactory;
import com.fate.pool.normal.NormalPool;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.airline.account.utils.MatchUtil.*;

/**
 * @author ydc
 * @date 2020/12/29.
 */
@Component
public class MoveComponent implements Constant {

    @Autowired
    private BatchService batchService;

//    @Autowired
//    private InsertService insertService;

    @Autowired
    private MoveLogMapper moveLogMapper;

    @Autowired
    private SalMapper salMapper;

    /**
     * 用于批量更新 E/R/U/I 状态
     *
     * @param batchSize 批处理大小
     * @param poolName  对象池key
     * @return 对象池
     */
    public NormalPool<CouponStatus> getRelationPool(Integer batchSize, String poolName) {
        return new NormalPool<>(batchSize, list -> {
            try {
                batchService.updateSegmentStatus(list);
            } catch (Exception e) {
                CouponStatus relation = list.get(0);
                String doc = relation.getDocumentCarrierIataNo() + relation.getDocumentNo();
                MoveLog log = new MoveLog(poolName, doc, e.getMessage());
                moveLogMapper.insertLog(log);
            }
        });
    }

    public PageHandler createSalPageHandler(CascadeNormalPoolFactory poolFactory, AllocateSource allocateSource, MoveService moveService) {
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

    /**
     * 只有国际改签关系
     * !!! 国内改签请修改MoveService实现
     *
     * @param ticketSize 批处理票数
     * @param batchSize  相关信息批处理数
     * @return 票相关对象池
     */
    public CascadeNormalPoolFactory getSalPoolFactory(Integer ticketSize, Integer batchSize) {
        CascadeNormalPoolFactory poolFactory = new CascadeNormalPoolFactory(ticketSize);
        CascadeNormalPool<Ticket> ticketPool = new CascadeNormalPool<>(batchSize, list -> {
            try {
                batchService.insertTicket(list);
            } catch (Exception e) {
                Ticket ticket = list.get(0);
                String doc = ticket.getDocumentCarrierIataNo() + ticket.getDocumentNo();
                MoveLog log = new MoveLog(POOL_KEY_TICKET, doc, e.getMessage());
                moveLogMapper.insertLog(log);
            }
        });
        poolFactory.addPool(POOL_KEY_TICKET, ticketPool);

        CascadeNormalPool<Segment> segmentPool = new CascadeNormalPool<>(batchSize, list -> {
            try {
                batchService.insertSegment(list);
            } catch (Exception e) {
                Segment segment = list.get(0);
                String doc = segment.getDocumentCarrierIataNo() + segment.getDocumentNo();
                MoveLog log = new MoveLog(POOL_KEY_SEGMENT, doc, e.getMessage());
                moveLogMapper.insertLog(log);
            }
        });
        poolFactory.addPool(POOL_KEY_SEGMENT, segmentPool);

        CascadeNormalPool<Tax> taxPool = new CascadeNormalPool<>(batchSize, list -> {
            try {
                batchService.insertTax(list);
            } catch (Exception e) {
                Tax tax = list.get(0);
                String doc = tax.getDocumentCarrierIataNo() + tax.getDocumentNo();
                MoveLog log = new MoveLog(POOL_KEY_TAX, doc, e.getMessage());
                moveLogMapper.insertLog(log);
            }
        });
        poolFactory.addPool(POOL_KEY_TAX, taxPool);

        CascadeNormalPool<CouponStatus> exchangePool = new CascadeNormalPool<>(batchSize, list -> {
            try {
                batchService.updateSegmentStatus(list);
            } catch (Exception e) {
                MoveLog log = new MoveLog(POOL_KEY_EXCHANGE, "", e.getMessage());
                moveLogMapper.insertLog(log);
            }
        });
        poolFactory.addPool(POOL_KEY_EXCHANGE, exchangePool);
        return poolFactory;
    }

    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void move(CascadeNormalPoolFactory poolFactory, Sal primarySal, MoveService moveService, String sourceName){
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
            //国内改签
            if (SYS_DP.equals(primarySal.getOutputSys())) {
                String status = primarySal.getExchOriginCouponNo();
                String[] relation = StringUtils.isBlank(status) ? "1234".split("") : status.split("");
                for (String cpnNo : relation) {
                    if (!COUPON_INVALID.equals(cpnNo)) {
                        CouponStatus couponStatus = new CouponStatus(primarySal.getExchOriginAirline(),
                                primarySal.getExchOriginTicketNo(), EtFormat.intFormat(cpnNo), STATUS_EXCHANGE);
                        poolFactory.appendObject(POOL_KEY_EXCHANGE, couponStatus);
                    }
                }
            } else {
                //国际改签
                List<Exchange> exchanges = moveService.getExchange(primarySal);
                for (Exchange exchange : exchanges) {
                    String[] status = exchange.getExchangeCouponUseIndicator().split("");
                    for (String cpnNo : status) {
                        if (!COUPON_INVALID.equals(cpnNo)) {
                            CouponStatus relation = new CouponStatus(exchange.getIssueInExchCarrierIataNo(), exchange.getIssueInExchDocumentNo(),
                                    EtFormat.intFormat(cpnNo), STATUS_EXCHANGE);
                            poolFactory.appendObject(POOL_KEY_EXCHANGE, relation);
                        }
                    }
                }
            }
        }

        try {
            poolFactory.afterAllAppend();
        } catch (Exception e) {
            MoveLog log = new MoveLog(primarySal.getAirline3code(), primarySal.getFirstTicketNo(), e.getMessage());
            moveLogMapper.insertLog(log);
        }
    }

    private static void addSeg(CascadeNormalPoolFactory factory, Sal sal) {
        if (isNumber(sal.getCouponUseIndicator())) {
            addNumberSeg(factory, sal);
        } else {
            addCharSeg(factory, sal);
        }
    }

    /**
     * 国内航段映射
     */
    private static void addCharSeg(CascadeNormalPoolFactory factory, Sal sal) {
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

    private static void addNumberSeg(CascadeNormalPoolFactory factory, Sal sal) {
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
