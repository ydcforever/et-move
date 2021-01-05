package com.airline.account.utils;

import com.airline.account.mapper.acca.RefDpMapper;
import com.airline.account.mapper.acca.RfdDpMapper;
import com.airline.account.mapper.acca.SalMapper;
import com.airline.account.mapper.et.MoveLogMapper;
import com.airline.account.model.acca.*;
import com.airline.account.model.et.*;
import com.airline.account.service.acca.RelationService;
import com.airline.account.service.move.BatchService;
import com.airline.account.service.move.InsertService;
import com.airline.account.service.move.LoadSourceService;
import com.airline.account.service.move.MoveService;
import com.fate.piece.PageHandler;
import com.fate.piece.PagePiece;
import com.fate.pool.PoolFinalHandler;
import com.fate.pool.normal.CascadeNormalPool;
import com.fate.pool.normal.CascadeNormalPoolFactory;
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
 * @date 2020/12/29.
 */
@Component
public class MoveComponent implements Constant{

    @Autowired
    private LoadSourceService loadSourceService;

    @Autowired
    private BatchService batchService;

    @Autowired
    private InsertService insertService;

    @Autowired
    private MoveLogMapper moveLogMapper;

    @Autowired
    private SalMapper salMapper;

    @Autowired
    private RelationService relationService;

    @Autowired
    private RfdDpMapper rfdDpMapper;

    @Autowired
    private RefDpMapper refDpMapper;

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

    /**
     * AUpl
     * !!! 承运插入ET待完成
     *
     * @param normalPool 对象池
     * @param allocateSource 资源分配
     * @return 分页处理
     */
    public PageHandler createUplPageHandler(NormalPool<String[]> normalPool, AllocateSource allocateSource) {
        return new PageHandler() {
            @Override
            public Integer count() {
                return relationService.countUplByAllocate(allocateSource);
            }

            @Override
            public void callback(PagePiece pagePiece) {
                List<AUpl> relations = relationService.queryUplByAllocate(allocateSource);
                //承运插入
                List<Upl> upls = getUpl(relations, allocateSource.getTableName());
                for(Upl upl : upls) {
                    try {
                        insertService.insertUplWithUpdate(upl);
                    } catch (Exception e) {
                        MoveLog log = new MoveLog(ERROR_UPL, upl.getDocumentCarrierIataNo() + upl.getDocumentNo(), e.getMessage());
                        moveLogMapper.insertLog(log);
                    }
                }

                for (AUpl aUpl : relations) {
                    try {
                        normalPool.beforeAppend();
                    } catch (Exception e) {
                        MoveLog log = new MoveLog(aUpl.getAirline3code(), aUpl.getTicketNo(), e.getMessage());
                        moveLogMapper.insertLog(log);
                    }
                    normalPool.appendObject(new String[]{aUpl.getAirline3code(), aUpl.getTicketNo(), aUpl.getCouponNo() + "", STATUS_FLOWN});
                }
            }
        };
    }

    /**
     * REF DP
     * !!! 国内退票关系入ET待完成
     *
     * @param normalPool 对象池
     * @param allocateSource 资源分配
     * @return 分页处理
     */
    public PageHandler createRefDpPageHandler(NormalPool<String[]> normalPool, AllocateSource allocateSource){
        return new PageHandler() {
            @Override
            public Integer count() {
                return refDpMapper.countRefDpByAllocate(allocateSource);
            }

            @Override
            public void callback(PagePiece pagePiece) {
                List<RefDp> relations = refDpMapper.queryRefDpByAllocate(allocateSource);
                for(RefDp refDp : relations) {
                    normalPool.appendObject(new String[]{refDp.getRefundOwnerCompany(), refDp.getRefundTicketNo(), refDp.getRefundRelationNo(), STATUS_REFUND});
                }
            }
        };
    }

    /**
     * RFD DP
     * !!! 国内改签关系入ET待完成
     *
     * @param normalPool 对象池
     * @param allocateSource 资源分配
     * @return 分页处理
     */
    public PageHandler createRfdDpPageHandler(NormalPool<String[]> normalPool, AllocateSource allocateSource){
        return new PageHandler() {
            @Override
            public Integer count() {
                return rfdDpMapper.countRfdDpByAllocate(allocateSource);
            }

            @Override
            public void callback(PagePiece pagePiece) {
                List<RfdDp> relations = rfdDpMapper.queryRfdDpByAllocate(allocateSource);
                for(RfdDp rfdDp : relations) {
                    normalPool.appendObject(new String[]{rfdDp.getTransferTicketCompany(), rfdDp.getTransferTicketNo(), rfdDp.getCouponNo()+"", STATUS_EXCHANGE});
                }
            }
        };
    }

    /**
     * Refund
     * !!! 国际退票关系入ET待完成
     *
     * @param normalPool 对象池
     * @param allocateSource 资源分配
     * @return 分页处理
     */
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
                                    normalPool.beforeAppend();
                                } catch (Exception e) {
                                    MoveLog log = new MoveLog(relation.getPrimaryTicketNo(), "", e.getMessage());
                                    moveLogMapper.insertLog(log);
                                }
                                String[] refund = new String[]{tkt.substring(0, 3), tkt.substring(3), cpn, STATUS_REFUND};
                                normalPool.appendObject(refund);
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

    /**
     * 只有国际改签关系
     * !!! 国内改签请修改MoveService实现
     *
     * @param ticketSize 批处理票数
     * @param batchSize 相关信息批处理数
     * @return 票相关对象池
     */
    public CascadeNormalPoolFactory getSalPoolFactory(Integer ticketSize, Integer batchSize) {
        CascadeNormalPoolFactory poolFactory = new CascadeNormalPoolFactory(ticketSize);
        CascadeNormalPool<Ticket> ticketPool = new CascadeNormalPool<>(batchSize, list -> {
            try {
                batchService.insertTicket(list);
            } catch (Exception e) {
                MoveLog log = new MoveLog(POOL_KEY_TICKET, "", e.getMessage());
                moveLogMapper.insertLog(log);
            }
        });
        poolFactory.addPool(POOL_KEY_TICKET, ticketPool);

        CascadeNormalPool<Segment> segmentPool = new CascadeNormalPool<>(batchSize, list -> {
            try {
                batchService.insertSegment(list);
            } catch (Exception e) {
                MoveLog log = new MoveLog(POOL_KEY_SEGMENT, "", e.getMessage());
                moveLogMapper.insertLog(log);
            }
        });
        poolFactory.addPool(POOL_KEY_SEGMENT, segmentPool);

        CascadeNormalPool<Tax> taxPool = new CascadeNormalPool<>(batchSize, list -> {
            try {
                batchService.insertTax(list);
            } catch (Exception e) {
                MoveLog log = new MoveLog(POOL_KEY_TAX, "", e.getMessage());
                moveLogMapper.insertLog(log);
            }
        });
        poolFactory.addPool(POOL_KEY_TAX, taxPool);

        CascadeNormalPool<String[]> exchangePool = new CascadeNormalPool<>(batchSize, list -> {
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

    /**
     * 执行按文件分页的数据
     *
     * @param finalHandler 对象池处理
     * @param allocateSource 资源分配
     * @param pageHandler 分页处理
     */
    public void executeByFile(PoolFinalHandler finalHandler, AllocateSource allocateSource, PageHandler pageHandler) {
        List<String> sources = loadSourceService.getSource(allocateSource.getConfigId());
        for (String file : sources) {
            allocateSource.setFileName(file);
            allocateSource.setTotal(pageHandler);
            try {
                finalHandler.finalHandle();
            } catch (Exception e) {
                e.printStackTrace();
            }
            loadSourceService.updateStatus(allocateSource.getConfigId(), file, "Y");
        }
    }

    /**
     * 执行按文件日期分页的数据
     *
     * @param finalHandler 对象池处理
     * @param allocateSource 资源分配
     * @param pageHandler 分页处理
     */
    public void executeByDate(PoolFinalHandler finalHandler, AllocateSource allocateSource, PageHandler pageHandler) {
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

    @Transactional(isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void move(CascadeNormalPoolFactory poolFactory, Sal primarySal, MoveService moveService) throws Exception{
        List<Sal> sals = moveService.getSal(primarySal);
        String cnjTktString = getCnjTktString(sals);
        for (Sal s : sals) {
            Ticket ticket = getTicket(s, cnjTktString, moveService.dataSource());
            poolFactory.appendObject(POOL_KEY_TICKET, ticket);
            addSeg(poolFactory, s);
        }
        List<Tax> taxes = moveService.getTax(primarySal);
        poolFactory.appendObject(POOL_KEY_TAX, taxes);
        if (STATUS_EXCHANGE.equals(primarySal.getSaleType())) {
            List<Relation> relations = moveService.getExchange(primarySal);
            for(Relation relation : relations){
                String ticketNo = relation.getOrgTicketNo();
                String[] status = relation.getCouponStatus().split("");
                for (String cpnNo : status) {
                    if (!COUPON_INVALID.equals(cpnNo)) {
                        poolFactory.appendObject(POOL_KEY_EXCHANGE, new String[]{ticketNo.substring(0, 3), ticketNo.substring(3), cpnNo, STATUS_EXCHANGE});
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
