package com.airline.account.controller;

import com.airline.account.service.move.MoveService;
import com.airline.account.utils.AllocateSource;
import com.airline.account.utils.CascadeNormalPoolFactory;
import com.airline.account.utils.MoveComponent;
import com.fate.piece.PageHandler;
import com.fate.pool.normal.NormalPool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author ydc
 * @date 2020/12/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class MoveControllerTest {

    @Autowired
    private MoveComponent moveComponent;

    @Resource(name = "moveDdpService")
    private MoveService moveDdpService;

    @Resource(name = "moveMdpService")
    private MoveService moveMdpService;

    @Resource(name = "moveDipService")
    private MoveService moveDipService;

    @Resource(name = "moveMipService")
    private MoveService moveMipService;

    @Test
    public void moveDip() {
        CascadeNormalPoolFactory poolFactory = moveComponent.getSalPoolFactory(1000, 1000);
        String sql = "select distinct t.issue_date from ACCA_SAL_IP_D t where t.source_name = ? order by t.issue_date";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_IP_D", "ACCA_SAL_IP_D", sql);
        PageHandler pageHandler = moveComponent.createSalPageHandler(poolFactory, allocateSource, moveDipService);
        moveComponent.execute(poolFactory, allocateSource, pageHandler);
    }

    @Test
    public void moveMip() {
        CascadeNormalPoolFactory poolFactory = moveComponent.getSalPoolFactory(1000, 1000);
        String sql = "select distinct t.issue_date from ACCA_SAL_IP_M t where t.source_name = ? order by t.issue_date";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_IP_M", "ACCA_SAL_IP_M", sql);
        PageHandler pageHandler = moveComponent.createSalPageHandler(poolFactory, allocateSource, moveMipService);
        moveComponent.execute(poolFactory, allocateSource, pageHandler);
    }

    @Test
    public void moveRefund() {
        NormalPool<String[]> normalPool = moveComponent.getRefundUplPool(1000, "Refund");
        String sql = "select distinct t.issue_date from ITAX_AUDITOR_REFUND t where t.ori_source = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ITAX_AUDITOR_REFUND", "Refund", sql);
        PageHandler pageHandler = moveComponent.createRefundPageHandler(normalPool, allocateSource);
        moveComponent.execute(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }

    @Test
    public void moveUplDpM() {
        NormalPool<String[]> normalPool = moveComponent.getRefundUplPool(1000, "ACCA_UPL_DP_M");
        String sql = "select distinct t.ticket_date from ACCA_UPL_DP_M t where t.source_name = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_UPL_DP_M", "ACCA_UPL_DP_M", sql);
        PageHandler pageHandler = moveComponent.createUplPageHandler(normalPool, allocateSource);
        moveComponent.execute(normalPool, allocateSource, pageHandler);
    }
}