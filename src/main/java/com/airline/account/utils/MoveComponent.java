package com.airline.account.utils;

import com.airline.account.mapper.acca.SalMapper;
import com.airline.account.mapper.et.MoveLogMapper;
import com.airline.account.model.acca.Sal;
import com.airline.account.model.allocate.AllocateSource;
import com.airline.account.model.et.*;
import com.airline.account.service.move.BatchService;
import com.airline.account.service.move.MoveService;
import com.airline.account.service.move.StatusService;
import com.fate.piece.PageHandler;
import com.fate.piece.PagePiece;
import com.fate.pool.PoolHandler;
import com.fate.pool.normal.CascadeNormalPool;
import com.fate.pool.normal.CascadeNormalPoolFactory;
import com.fate.pool.normal.NormalPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.airline.account.utils.MatchUtil.getCnjTktString;
import static com.airline.account.utils.MatchUtil.getTicket;

/**
 * @author ydc
 * @date 2020/12/29.
 */
@Component
@Deprecated
public class MoveComponent implements Constant {

    @Autowired
    private BatchService batchService;

    @Autowired
    private MoveLogMapper moveLogMapper;

    @Autowired
    private SalMapper salMapper;

    @Autowired
    private StatusService statusService;

    /**
     * 用于批量更新 E/R/U/I 状态
     *
     * @param batchSize 批处理大小
     * @param poolName  对象池key
     * @return 对象池
     */
    public NormalPool<Relation> getRelationPool(Integer batchSize, String poolName) {
        return new NormalPool<>(batchSize, new PoolHandler<Relation>() {

            @Override
            public void handle(List<Relation> list) throws Exception {
                try {
                    statusService.updateSegmentStatus(list);
                } catch (Exception e) {
                    Relation relation = list.get(0);
                    String tktn = relation.getOperateDocumentCarrierIataNo() + relation.getOperateDocumentNo();
                    MoveLog log = new MoveLog(poolName, tktn, e.getMessage());
                    moveLogMapper.insertLog(log);
                }
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
        CascadeNormalPool<Ticket> ticketPool = new CascadeNormalPool<>(batchSize, new PoolHandler<Ticket>() {

            @Override
            public void handle(List<Ticket> list) throws Exception {

                try {
                    batchService.insertTicket(list);
                } catch (Exception e) {
                    Ticket ticket = list.get(0);
                    String doc = ticket.getDocumentCarrierIataNo() + ticket.getDocumentNo();
                    MoveLog log = new MoveLog(POOL_KEY_TICKET, doc, e.getMessage());
                    moveLogMapper.insertLog(log);
                }
            }
        });
        poolFactory.addPool(POOL_KEY_TICKET, ticketPool);

        CascadeNormalPool<Segment> segmentPool = new CascadeNormalPool<>(batchSize, new PoolHandler<Segment>() {
            @Override
            public void handle(List<Segment> list) throws Exception {
                try {
                    batchService.insertSegment(list);
                } catch (Exception e) {
                    Segment segment = list.get(0);
                    String doc = segment.getDocumentCarrierIataNo() + segment.getDocumentNo();
                    MoveLog log = new MoveLog(POOL_KEY_SEGMENT, doc, e.getMessage());
                    moveLogMapper.insertLog(log);
                }
            }
        });
        poolFactory.addPool(POOL_KEY_SEGMENT, segmentPool);

        CascadeNormalPool<Tax> taxPool = new CascadeNormalPool<>(batchSize, new PoolHandler<Tax>() {
            @Override
            public void handle(List<Tax> list) throws Exception {
                try {
                    batchService.insertTax(list);
                } catch (Exception e) {
                    Tax tax = list.get(0);
                    String doc = tax.getDocumentCarrierIataNo() + tax.getDocumentNo();
                    MoveLog log = new MoveLog(POOL_KEY_TAX, doc, e.getMessage());
                    moveLogMapper.insertLog(log);
                }
            }
        });
        poolFactory.addPool(POOL_KEY_TAX, taxPool);

        CascadeNormalPool<Relation> exchangePool = new CascadeNormalPool<>(batchSize, new PoolHandler<Relation>() {
            @Override
            public void handle(List<Relation> list) throws Exception {
                try {
                    statusService.updateSegmentStatus(list);
                } catch (Exception e) {
                    MoveLog log = new MoveLog(POOL_KEY_EXCHANGE, "", e.getMessage());
                    moveLogMapper.insertLog(log);
                }
            }
        });
        poolFactory.addPool(POOL_KEY_EXCHANGE, exchangePool);
        return poolFactory;
    }

    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void move(CascadeNormalPoolFactory poolFactory, Sal primarySal, MoveService moveService, String sourceName) {
        List<Sal> salList = moveService.getSal(primarySal);
        String cnjTktString = getCnjTktString(salList);
        for (Sal s : salList) {
            Ticket ticket = getTicket(s, cnjTktString, sourceName);
            poolFactory.appendObject(POOL_KEY_TICKET, ticket);
            PoolUtil.addSeg(poolFactory, s);
        }
        List<Tax> taxes = moveService.getTax(primarySal);
        poolFactory.appendObject(POOL_KEY_TAX, taxes);
        if (STATUS_EXCHANGE.equals(primarySal.getSaleType())) {
            //改签
            List<Relation> exchanges = moveService.getExchange(primarySal);
            for (Relation exchange : exchanges) {
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
            poolFactory.afterAllAppend();
        } catch (Exception e) {
            String tktn = primarySal.getAirline3code() + primarySal.getFirstTicketNo();
            MoveLog log = new MoveLog(ERROR_SAL, tktn, e.getMessage());
            moveLogMapper.insertLog(log);
        }
    }
}
