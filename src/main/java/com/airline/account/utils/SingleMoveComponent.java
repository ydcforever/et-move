package com.airline.account.utils;

import com.airline.account.mapper.acca.SalMapper;
import com.airline.account.mapper.et.MoveLogMapper;
import com.airline.account.model.acca.Sal;
import com.airline.account.model.allocate.AllocateSource;
import com.airline.account.model.et.*;
import com.airline.account.service.move.ExchangeUseService;
import com.airline.account.service.move.InsertService;
import com.airline.account.service.move.MoveService;
import com.airline.account.service.move.StatusService;
import com.fate.piece.PageHandler;
import com.fate.piece.PagePiece;
import com.fate.pool.PoolHandler;
import com.fate.pool.normal.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.airline.account.utils.MatchUtil.*;
import static com.airline.account.utils.PoolUtil.addSeg;

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

    /**
     * 只有国际改签关系
     * !!! 国内改签请修改MoveService实现
     *
     * @return 票相关对象池
     */
    public CascadeNormalPoolFactory getSalPoolFactory() {
        CascadeNormalPoolFactory poolFactory = new CascadeNormalPoolFactory(1);
        CascadeNormalPool<Ticket> ticketPool = new CascadeNormalPool<>(1, new PoolHandler<Ticket>() {
            @Override
            public void singleHandle(Ticket ticket) throws Exception {
                insertService.insertTicketWithUpdate(ticket);
            }
        });
        poolFactory.addPool(POOL_KEY_TICKET, ticketPool);

        CascadeNormalPool<Segment> segmentPool = new CascadeNormalPool<>(1, new PoolHandler<Segment>() {
            @Override
            public void singleHandle(Segment segment) throws Exception {
                insertService.insertSegmentWithUpdate(segment);
            }
        });
        poolFactory.addPool(POOL_KEY_SEGMENT, segmentPool);

        CascadeNormalPool<Tax> taxPool = new CascadeNormalPool<>(1, new PoolHandler<Tax>() {
            @Override
            public void singleHandle(Tax tax) throws Exception {
                insertService.insertTaxWithUpdate(tax);
            }
        });
        poolFactory.addPool(POOL_KEY_TAX, taxPool);

        CascadeNormalPool<Relation> exchangePool = new CascadeNormalPool<>(100, new PoolHandler<Relation>() {
            @Override
            public void handle(List<Relation> list) throws Exception {
                statusService.updateSegmentStatus(list);
            }
        });

        poolFactory.addPool(POOL_KEY_EXCHANGE, exchangePool);
        return poolFactory;
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

    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void move(CascadeNormalPoolFactory poolFactory, Sal primarySal, MoveService moveService, String sourceName) {
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
            List<Relation> exchanges = moveService.getExchange(primarySal);
            for (Relation exchange : exchanges) {
                //改签关系插入，精细化切点
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
            poolFactory.afterAllAppend();
            // 精细化切点
        } catch (Exception e) {
            String tktn = primarySal.getAirline3code() + primarySal.getFirstTicketNo();
            MoveLog log = new MoveLog(ERROR_SAL, tktn, e.getMessage());
            moveLogMapper.insertLog(log);
        }
    }
}
