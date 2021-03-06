package com.airline.account.controller;

import com.airline.account.model.allocate.AllocateSource;
import com.airline.account.model.et.Relation;
import com.airline.account.service.acca.*;
import com.airline.account.service.allocate.StatisticService;
import com.airline.account.service.allocate.ThreadLogService;
import com.airline.account.service.allocate.WorkService;
import com.airline.account.service.move.LoadSourceService;
import com.airline.account.service.move.MoveService;
import com.airline.account.utils.AllocateRunnable;
import com.airline.account.utils.Handler;
import com.airline.account.utils.SingleMoveComponent;
import com.fate.piece.PageHandler;
import com.fate.pool.normal.CascadeNormalPoolFactory;
import com.fate.pool.normal.NormalPool;
import com.fate.thread.ResourcePoolUtil;
import com.fate.thread.ResourceRunnable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author ydc
 * @date 2020/12/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class MoveControllerTest {

    @Autowired
    private SingleMoveComponent moveComponent;

    @Resource(name = "moveDdpService")
    private MoveService moveDdpService;

    @Resource(name = "moveMdpService")
    private MoveService moveMdpService;

    @Resource(name = "moveDipService")
    private MoveService moveDipService;

    @Resource(name = "moveMipService")
    private MoveService moveMipService;
    
    @Autowired
    private LoadSourceService loadSourceService;
    
    @Autowired
    private RefundService refundService;
    
    @Autowired
    private ExchangeService exchangeService;
    
    @Autowired
    private RefService refService;
    
    @Autowired 
    private UplService uplService;

    @Autowired
    private IwbService iwbService;

    @Autowired
    private ThreadLogService threadLogService;

    @Autowired
    private StatisticService statisticService;

    @Autowired
    private WorkService workService;

    @Test
    public void moveDip() {
        CascadeNormalPoolFactory poolFactory = moveComponent.getSalPoolFactory();
        String sql = "select distinct t.issue_date from ACCA_SAL_IP_D t where t.source_name = ? order by t.issue_date";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_IP_D", "ACCA_SAL_IP_D", sql);
        PageHandler pageHandler = moveComponent.createSalPageHandler(poolFactory, allocateSource, moveDipService);
        loadSourceService.executeByDate(poolFactory, allocateSource, pageHandler);
        poolFactory.destroy();
    }

    @Test
    public void moveMip() {
        CascadeNormalPoolFactory poolFactory = moveComponent.getSalPoolFactory();
        String sql = "select distinct t.issue_date from ACCA_SAL_IP_M t where t.source_name = ? order by t.issue_date";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_IP_M", "ACCA_SAL_IP_M", sql);
        PageHandler pageHandler = moveComponent.createSalPageHandler(poolFactory, allocateSource, moveMipService);
        loadSourceService.executeByDate(poolFactory, allocateSource, pageHandler);
        poolFactory.destroy();
    }

    @Test
    public void moveMdp() {
        String sql = "select distinct t.issue_date from ACCA_SAL_DP_M t where t.source_name = ? order by t.issue_date";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_DP_M", "ACCA_SAL_DP_M", sql);
        LinkedBlockingQueue<AllocateSource> queue = loadSourceService.getResourceByDate(allocateSource);
        ThreadPoolExecutor executor = ResourcePoolUtil.createThreadPool(queue, new ResourceRunnable<AllocateSource>() {
            @Override
            public Runnable generate(LinkedBlockingQueue<AllocateSource> resource, int sequence) {
                CascadeNormalPoolFactory poolFactory = moveComponent.getSalPoolFactory();
                return new AllocateRunnable(queue, String.valueOf(sequence), threadLogService, poolFactory, new Handler() {
                    @Override
                    public PageHandler generate(AllocateSource allocateSource) {
                        return moveComponent.createSalPageHandler(poolFactory, allocateSource, moveMdpService);
                    }
                });
            }
        }, 4, 60);
        ResourcePoolUtil.shutdown(executor, 600);
    }

    @Test
    public void moveMdpNew() {
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_DP_M");
//        allocateSource.setDateColName("退票或有不同");
        LinkedBlockingQueue<AllocateSource> queue = statisticService.getResource(allocateSource);
        ThreadPoolExecutor executor = ResourcePoolUtil.createThreadPool(queue, new ResourceRunnable<AllocateSource>() {
            @Override
            public Runnable generate(LinkedBlockingQueue<AllocateSource> resource, int sequence) {
                CascadeNormalPoolFactory poolFactory = moveComponent.getSalPoolFactory();
                return new AllocateRunnable(queue, String.valueOf(sequence), threadLogService, poolFactory, new Handler() {
                    @Override
                    public PageHandler generate(AllocateSource allocateSource) {
                        return moveComponent.createSalPageHandler(poolFactory, allocateSource, moveMdpService);
                    }
                });
            }
        }, 4, 60);
        ResourcePoolUtil.shutdown(executor, 600);
        workService.updateWorkRunning(allocateSource.getTableName());
    }

    @Test
    public void moveRefund() {
        NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "Refund");
        String sql = "select distinct t.issue_date from ITAX_AUDITOR_REFUND t where t.ori_source = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ITAX_AUDITOR_REFUND", "Refund", sql);
        PageHandler pageHandler = refundService.createPageHandler(normalPool, allocateSource);
        loadSourceService.executeByDate(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }

    @Test
    public void moveUplDpM() {
        NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "ACCA_UPL_DP_M");
        String sql = "select distinct t.ticket_date from ACCA_UPL_DP_M t where t.source_name = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_UPL_DP_M", "ACCA_UPL_DP_M", sql);
        PageHandler pageHandler = uplService.createPageHandler(normalPool, allocateSource);
        loadSourceService.executeByDate(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }

    @Test
    public void moveRefDp() {
        NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "ACCA_REF_DP_M");
        String sql = "select distinct t.refund_date from ACCA_REF_DP_M t where t.source_name = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_REF_DP_M", "ACCA_REF_DP_M", sql);
        PageHandler pageHandler = refService.createPageHandler(normalPool, allocateSource);
        loadSourceService.executeByDate(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }

    @Test
    public void moveExchange() {
        NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "Exchange");
        String sql = "select distinct t.issue_date from ITAX_AUDITOR_EXCHANGE t where t.ori_source = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ITAX_AUDITOR_EXCHANGE", "Exchange", sql);
        PageHandler pageHandler = exchangeService.createPageHandler(normalPool, allocateSource);
        loadSourceService.executeByDate(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }

    @Test
    public void moveDdp() {
        CascadeNormalPoolFactory poolFactory = moveComponent.getSalPoolFactory();
        String sql = "select distinct t.issue_date from ACCA_SAL_DP_D t where t.source_name = ? order by t.issue_date";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_DP_D", "ACCA_SAL_DP_D", sql);
        PageHandler pageHandler = moveComponent.createSalPageHandler(poolFactory, allocateSource, moveDdpService);
        loadSourceService.executeByDate(poolFactory, allocateSource, pageHandler);
        poolFactory.destroy();
    }

    @Test
    public void moveIwbDpM() {
        NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "ACCA_IWB_DP_M");
        String sql = "select distinct t.ticket_date from ACCA_IWB_DP_M t where t.source_name = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_IWB_DP_M", "ACCA_IWB_DP_M", sql);
        PageHandler pageHandler = iwbService.createPageHandler(normalPool, allocateSource);
        loadSourceService.executeByDate(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }
}