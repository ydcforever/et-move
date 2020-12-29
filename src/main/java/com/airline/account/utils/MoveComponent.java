package com.airline.account.utils;

import com.airline.account.mapper.acca.SalMapper;
import com.airline.account.mapper.et.MoveLogMapper;
import com.airline.account.model.acca.AUpl;
import com.airline.account.model.acca.Relation;
import com.airline.account.model.acca.Sal;
import com.airline.account.model.et.MoveLog;
import com.airline.account.model.et.Segment;
import com.airline.account.model.et.Tax;
import com.airline.account.model.et.Ticket;
import com.airline.account.service.acca.RelationService;
import com.airline.account.service.move.BatchService;
import com.airline.account.service.move.LoadSourceService;
import com.airline.account.service.move.MoveService;
import com.fate.piece.PageHandler;
import com.fate.piece.PagePiece;
import com.fate.pool.PoolFinalHandler;
import com.fate.pool.normal.CascadeNormalPool;
import com.fate.pool.normal.NormalPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.airline.account.utils.MatchUtil.*;

/**
 * @author ydc
 * @date 2020/12/29.
 */
@Component
public class MoveComponent {

    private static final String COUPON_STATUS_EXCHANGE = "E";

    @Autowired
    private LoadSourceService loadSourceService;

    @Autowired
    private BatchService batchService;

    @Autowired
    private MoveLogMapper moveLogMapper;

    @Autowired
    private SalMapper salMapper;

    @Autowired
    private RelationService relationService;

    public NormalPool<String[]> getRefundUplPool(Integer batchSize, String poolName) {
        return new NormalPool<>(batchSize, list -> {
            try {
                batchService.updateSegmentStatus(list);
            } catch (Exception e) {
                MoveLog log = new MoveLog(poolName, "", e.getMessage());
                moveLogMapper.insertLog(log);
            }
        });
    }

    public PageHandler createUplPageHandler(NormalPool<String[]> normalPool, AllocateSource allocateSource) {
        return new PageHandler() {
            @Override
            public Integer count() {
                return relationService.countUplByAllocate(allocateSource);
            }

            @Override
            public void callback(PagePiece pagePiece) {
                List<AUpl> relations = relationService.queryUplByAllocate(allocateSource);
                for (AUpl aUpl : relations) {
                    try {
                        normalPool.beforeAppend();
                    } catch (Exception e) {
                        MoveLog log = new MoveLog(aUpl.getAirline3code(), aUpl.getTicketNo(), e.getMessage());
                        moveLogMapper.insertLog(log);
                    }
                    normalPool.appendObject(new String[]{aUpl.getAirline3code(), aUpl.getTicketNo(), aUpl.getCouponNo() + "", "F"});
                }
            }
        };
    }

    public PageHandler createRefundPageHandler(NormalPool<String[]> normalPool, AllocateSource allocateSource) {
        return new PageHandler() {
            @Override
            public Integer count() {
                return relationService.countRefundByAllocate(allocateSource);
            }

            @Override
            public void callback(PagePiece pagePiece) {
                List<Relation> relations = relationService.queryRefundByAllocate(allocateSource);
                for (Relation relation : relations) {
                    String[] tkts = relation.getTicketNoStr().split(";");
                    String[] tktStatus = relation.getCouponStatus().split(";");
                    for (int i = 0, len = tktStatus.length; i < len; i++) {
                        String[] cpns = tktStatus[i].split("");
                        String tkt = tkts[i];
                        for (String cpn : cpns) {
                            if (!"0".equals(cpn)) {
                                try {
                                    normalPool.beforeAppend();
                                } catch (Exception e) {
                                    MoveLog log = new MoveLog(relation.getPrimaryTicketNo(), "", e.getMessage());
                                    moveLogMapper.insertLog(log);
                                }
                                normalPool.appendObject(new String[]{tkt.substring(0, 3), tkt.substring(3), cpn, "R"});
                            }
                        }
                    }
                }
            }
        };
    }

    public PageHandler createSalPageHandler(CascadeNormalPoolFactory poolFactory, AllocateSource allocateSource, MoveService moveService) {
        return new PageHandler() {
            @Override
            public Integer count() {
                return salMapper.countPrimaryByAllocate(allocateSource);
            }

            @Override
            public void callback(PagePiece pagePiece) {
                List<Sal> list = salMapper.queryPrimaryByAllocate(allocateSource);
                for (Sal sal : list) {
                    try {
                        move(poolFactory, sal, moveService);
                    } catch (Exception e) {
                        MoveLog log = new MoveLog(sal.getAirline3code(), sal.getFirstTicketNo(), e.getMessage());
                        moveLogMapper.insertLog(log);
                    }
                }
            }
        };
    }

    public CascadeNormalPoolFactory getSalPoolFactory(Integer ticketSize, Integer batchSize) {
        CascadeNormalPoolFactory poolFactory = new CascadeNormalPoolFactory(ticketSize);
        CascadeNormalPool<Ticket> ticketPool = new CascadeNormalPool<>(batchSize, list -> {
            try {
                batchService.insertTicket(list);
            } catch (Exception e) {
                MoveLog log = new MoveLog("Ticket", "", e.getMessage());
                moveLogMapper.insertLog(log);
            }
        });
        poolFactory.setTicketPool(ticketPool);

        CascadeNormalPool<Segment> segmentPool = new CascadeNormalPool<>(batchSize, list -> {
            try {
                batchService.insertSegment(list);
            } catch (Exception e) {
                MoveLog log = new MoveLog("Segment", "", e.getMessage());
                moveLogMapper.insertLog(log);
            }
        });
        poolFactory.setSegmentPool(segmentPool);

        CascadeNormalPool<Tax> taxPool = new CascadeNormalPool<>(batchSize, list -> {
            try {
                batchService.insertTax(list);
            } catch (Exception e) {
                MoveLog log = new MoveLog("Tax", "", e.getMessage());
                moveLogMapper.insertLog(log);
            }
        });
        poolFactory.setTaxPool(taxPool);

        CascadeNormalPool<String[]> exchangePool = new CascadeNormalPool<>(batchSize, list -> {
            try {
                batchService.updateSegmentStatus(list);
            } catch (Exception e) {
                MoveLog log = new MoveLog("Exchange", "", e.getMessage());
                moveLogMapper.insertLog(log);
            }
        });
        poolFactory.setExchangePool(exchangePool);
        return poolFactory;
    }

    public void execute(PoolFinalHandler finalHandler, AllocateSource allocateSource, PageHandler pageHandler) {
        List<String> sources = loadSourceService.getSource(allocateSource.getConfigId());
        for (String file : sources) {
            allocateSource.setFileName(file);
            List<String> issueDates = loadSourceService.getIssueDates(allocateSource);
            for (String issue : issueDates) {
                allocateSource.setCurrentIssueDate(issue);
                allocateSource.setTotal(pageHandler);
                try {
                    finalHandler.finalHandle();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            loadSourceService.updateStatus(allocateSource.getConfigId(), file, "Y");
        }
    }

    private void move(CascadeNormalPoolFactory poolFactory, Sal primarySal, MoveService moveService) throws Exception{
        List<Sal> sals = moveService.getSal(primarySal);
        String cnjTktString = getCnjTktString(sals);
        for (Sal s : sals) {
            Ticket ticket = getTicket(s, cnjTktString, moveService.dataSource());
            poolFactory.ticketPool.appendObject(ticket);
            addSeg(poolFactory, s);
        }
        List<Tax> taxes = moveService.getTax(primarySal);
        poolFactory.taxPool.appendObject(taxes);
        if (COUPON_STATUS_EXCHANGE.equals(primarySal.getSaleType())) {
            List<Relation> relations = moveService.getExchange(primarySal);
            for(Relation relation : relations){
                String ticketNo = relation.getOrgTicketNo();
                String[] status = relation.getCouponStatus().split("");
                for (String cpnNo : status) {
                    if (!"0".equals(cpnNo)) {
                        poolFactory.exchangePool.appendObject(new String[]{ticketNo.substring(0, 3), ticketNo.substring(3), cpnNo, COUPON_STATUS_EXCHANGE});
                    }
                }
            }
        }
        poolFactory.afterAllAppend();
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
            if (!"V".equals(status[i])) {
                if (i == 0) {
                    Segment seg = buildSeg1(sal, status[i]);
                    factory.segmentPool.appendObject(seg);
                } else if (i == 1) {
                    Segment seg = buildSeg2(sal, status[i]);
                    factory.segmentPool.appendObject(seg);
                } else if (i == 2) {
                    Segment seg = buildSeg3(sal, status[i]);
                    factory.segmentPool.appendObject(seg);
                } else if (i == 3) {
                    Segment seg = buildSeg4(sal, status[i]);
                    factory.segmentPool.appendObject(seg);
                }
            }
        }
    }

    private static void addNumberSeg(CascadeNormalPoolFactory factory, Sal sal) {
        String[] status = sal.getCouponUseIndicator().split("");
        for (String s : status) {
            if ("1".equals(s)) {
                Segment seg = buildSeg1(sal, "F");
                factory.segmentPool.appendObject(seg);
            } else if ("2".equals(s)) {
                Segment seg = buildSeg2(sal, "F");
                factory.segmentPool.appendObject(seg);
            } else if ("3".equals(s)) {
                Segment seg = buildSeg3(sal, "F");
                factory.segmentPool.appendObject(seg);
            } else if ("4".equals(s)) {
                Segment seg = buildSeg4(sal, "F");
                factory.segmentPool.appendObject(seg);
            }
        }
    }

    private static boolean isNumber(String str) {
        return str.matches(".*[0-9]+.*");
    }
}
