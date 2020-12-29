package com.airline.account.controller;

import com.airline.account.service.move.MoveService;
import com.airline.account.utils.AllocateSource;
import com.airline.account.utils.CascadeNormalPoolFactory;
import com.airline.account.utils.MoveComponent;
import com.fate.piece.PageHandler;
import com.fate.pool.normal.NormalPool;
import com.fate.schedule.SteerableSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * @author ydc
 * @date 2020/11/25
 */
@RestController
@RequestMapping("/et")
public class MoveController {

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

    @RequestMapping(value = "/moveDIp.do", method = RequestMethod.POST)
    @ResponseBody
    @SteerableSchedule(id = "ACCA_SAL_IP_D", cron = "0 0 0 * * ?")
    public void moveDip() {
        CascadeNormalPoolFactory poolFactory = moveComponent.getSalPoolFactory(1000, 1000);
        String sql = "select distinct t.issue_date from ACCA_SAL_IP_D t where t.source_name = ? order by t.issue_date";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_IP_D", "ACCA_SAL_IP_D", sql);
        PageHandler pageHandler = moveComponent.createSalPageHandler(poolFactory, allocateSource, moveDipService);
        moveComponent.execute(poolFactory, allocateSource, pageHandler);
    }

    @RequestMapping(value = "/moveDDp.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_SAL_DP_D", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveDdp() {
        CascadeNormalPoolFactory poolFactory = moveComponent.getSalPoolFactory(1000, 1000);
        String sql = "select distinct t.issue_date from ACCA_SAL_DP_D t where t.source_name = ? order by t.issue_date";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_DP_D", "ACCA_SAL_DP_D", sql);
        PageHandler pageHandler = moveComponent.createSalPageHandler(poolFactory, allocateSource, moveDdpService);
        moveComponent.execute(poolFactory, allocateSource, pageHandler);
    }

    @RequestMapping(value = "/moveMIp.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_SAL_IP_M", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveMip() {
        CascadeNormalPoolFactory poolFactory = moveComponent.getSalPoolFactory(1000, 1000);
        String sql = "select distinct t.issue_date from ACCA_SAL_IP_M t where t.source_name = ? order by t.issue_date";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_IP_M", "ACCA_SAL_IP_M", sql);
        PageHandler pageHandler = moveComponent.createSalPageHandler(poolFactory, allocateSource, moveMipService);
        moveComponent.execute(poolFactory, allocateSource, pageHandler);
    }

    @RequestMapping(value = "/moveMDp.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_SAL_DP_M", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveMdp() {
        CascadeNormalPoolFactory poolFactory = moveComponent.getSalPoolFactory(1000, 1000);
        String sql = "select distinct t.issue_date from ACCA_SAL_DP_M t where t.source_name = ? order by t.issue_date";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_DP_M", "ACCA_SAL_DP_M", sql);
        PageHandler pageHandler = moveComponent.createSalPageHandler(poolFactory, allocateSource, moveMdpService);
        moveComponent.execute(poolFactory, allocateSource, pageHandler);
    }

    @RequestMapping(value = "/moveRefund.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "Refund", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveRefund() {
        NormalPool<String[]> normalPool = moveComponent.getRefundUplPool(1000, "Refund");
        String sql = "select distinct t.issue_date from ITAX_AUDITOR_REFUND t where t.ori_source = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ITAX_AUDITOR_REFUND", "Refund", sql);
        PageHandler pageHandler = moveComponent.createRefundPageHandler(normalPool, allocateSource);
        moveComponent.execute(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }

    @RequestMapping(value = "/moveUplIpM.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_UPL_IP_M", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveUplIpM() {
        NormalPool<String[]> normalPool = moveComponent.getRefundUplPool(1000, "ACCA_UPL_IP_M");
        String sql = "select distinct t.ticket_date from ACCA_UPL_IP_M t where t.source_name = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_UPL_IP_M", "ACCA_UPL_IP_M", sql);
        PageHandler pageHandler = moveComponent.createUplPageHandler(normalPool, allocateSource);
        moveComponent.execute(normalPool, allocateSource, pageHandler);
    }

    @RequestMapping(value = "/moveUplIpD.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_UPL_IP_D", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveUplIpD() {
        NormalPool<String[]> normalPool = moveComponent.getRefundUplPool(1000, "ACCA_UPL_IP_D");
        String sql = "select distinct t.ticket_date from ACCA_UPL_IP_D t where t.source_name = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_UPL_IP_D", "ACCA_UPL_IP_D", sql);
        PageHandler pageHandler = moveComponent.createUplPageHandler(normalPool, allocateSource);
        moveComponent.execute(normalPool, allocateSource, pageHandler);
    }

    @RequestMapping(value = "/moveUplDpM.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_UPL_DP_M", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveUplDpM() {
        NormalPool<String[]> normalPool = moveComponent.getRefundUplPool(1000, "ACCA_UPL_DP_M");
        String sql = "select distinct t.ticket_date from ACCA_UPL_DP_M t where t.source_name = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_UPL_DP_M", "ACCA_UPL_DP_M", sql);
        PageHandler pageHandler = moveComponent.createUplPageHandler(normalPool, allocateSource);
        moveComponent.execute(normalPool, allocateSource, pageHandler);
    }

    @RequestMapping(value = "/moveUplDpD.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_UPL_DP_D", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveUplDpD() {
        NormalPool<String[]> normalPool = moveComponent.getRefundUplPool(1000, "ACCA_UPL_DP_D");
        String sql = "select distinct t.ticket_date from ACCA_UPL_DP_D t where t.source_name = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_UPL_DP_D", "ACCA_UPL_DP_D", sql);
        PageHandler pageHandler = moveComponent.createUplPageHandler(normalPool, allocateSource);
        moveComponent.execute(normalPool, allocateSource, pageHandler);
    }
}
