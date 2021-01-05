package com.airline.account.controller;

import com.airline.account.service.move.MoveService;
import com.airline.account.utils.AllocateSource;
import com.fate.pool.normal.CascadeNormalPoolFactory;
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

    /**
     * ACCA 国际销售日数据 含有国际改签 状态
     */
    @RequestMapping(value = "/moveDIp.do", method = RequestMethod.POST)
    @ResponseBody
    @SteerableSchedule(id = "ACCA_SAL_IP_D", cron = "0 0 0 * * ?")
    public void moveDip() {
        CascadeNormalPoolFactory poolFactory = moveComponent.getSalPoolFactory(1000, 1);
        String sql = "select distinct t.issue_date from ACCA_SAL_IP_D t where t.source_name = ? order by t.issue_date";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_IP_D", "ACCA_SAL_IP_D", sql);
        PageHandler pageHandler = moveComponent.createSalPageHandler(poolFactory, allocateSource, moveDipService);
        moveComponent.executeByDate(poolFactory, allocateSource, pageHandler);
        poolFactory.destroy();
    }

    /**
     * ACCA 国内销售日数据 含有国际改签 状态
     */
    @RequestMapping(value = "/moveDDp.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_SAL_DP_D", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveDdp() {
        CascadeNormalPoolFactory poolFactory = moveComponent.getSalPoolFactory(1000, 1);
        String sql = "select distinct t.issue_date from ACCA_SAL_DP_D t where t.source_name = ? order by t.issue_date";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_DP_D", "ACCA_SAL_DP_D", sql);
        PageHandler pageHandler = moveComponent.createSalPageHandler(poolFactory, allocateSource, moveDdpService);
        moveComponent.executeByDate(poolFactory, allocateSource, pageHandler);
        poolFactory.destroy();
    }

    /**
     * ACCA 国际销售月数据 含有国际改签 状态
     */
    @RequestMapping(value = "/moveMIp.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_SAL_IP_M", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveMip() {
        //值为1 适用主键重复时更新数据
        CascadeNormalPoolFactory poolFactory = moveComponent.getSalPoolFactory(1000, 1);
        String sql = "select distinct t.issue_date from ACCA_SAL_IP_M t where t.source_name = ? order by t.issue_date";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_IP_M", "ACCA_SAL_IP_M", sql);
        PageHandler pageHandler = moveComponent.createSalPageHandler(poolFactory, allocateSource, moveMipService);
        moveComponent.executeByDate(poolFactory, allocateSource, pageHandler);
        poolFactory.destroy();
    }

    /**
     * ACCA 国内销售月数据 含有国际改签 状态
     */
    @RequestMapping(value = "/moveMDp.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_SAL_DP_M", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveMdp() {
        CascadeNormalPoolFactory poolFactory = moveComponent.getSalPoolFactory(1000, 1);
        String sql = "select distinct t.issue_date from ACCA_SAL_DP_M t where t.source_name = ? order by t.issue_date";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_DP_M", "ACCA_SAL_DP_M", sql);
        PageHandler pageHandler = moveComponent.createSalPageHandler(poolFactory, allocateSource, moveMdpService);
        moveComponent.executeByDate(poolFactory, allocateSource, pageHandler);
        poolFactory.destroy();
    }

    /**
     * 无文件提供
     * ACCA 国内改签 状态
     */
    @RequestMapping(value = "/moveRfdDp.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "moveRfdDp", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveRfdDp() {
        NormalPool<String[]> normalPool = moveComponent.getRefundUplPool(1000, "ACCA_RFD_DP_M");
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_RFD_DP_M", "ACCA_RFD_DP_M", "");
        PageHandler pageHandler = moveComponent.createRfdDpPageHandler(normalPool, allocateSource);
        moveComponent.executeByFile(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }

    /**
     * 国际退票 状态
     */
    @RequestMapping(value = "/moveRefund.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "Refund", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveRefund() {
        NormalPool<String[]> normalPool = moveComponent.getRefundUplPool(1000, "Refund");
        String sql = "select distinct t.issue_date from ITAX_AUDITOR_REFUND t where t.ori_source = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ITAX_AUDITOR_REFUND", "Refund", sql);
        PageHandler pageHandler = moveComponent.createRefundPageHandler(normalPool, allocateSource);
        moveComponent.executeByDate(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }

    /**
     * ACCA 国内退票 状态
     */
    @RequestMapping(value = "/moveRefDp.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "moveRefDp", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveRefDp() {
        NormalPool<String[]> normalPool = moveComponent.getRefundUplPool(1000, "ACCA_REF_DP_M");
        String sql = "select distinct t.refund_date from ACCA_REF_DP_M t where t.source_name = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_REF_DP_M", "ACCA_REF_DP_M", sql);
        PageHandler pageHandler = moveComponent.createRefDpPageHandler(normalPool, allocateSource);
        moveComponent.executeByDate(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }

    /**
     * ACCA 国际承运月数据 状态
     */
    @RequestMapping(value = "/moveUplIpM.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_UPL_IP_M", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveUplIpM() {
        NormalPool<String[]> normalPool = moveComponent.getRefundUplPool(1000, "ACCA_UPL_IP_M");
        String sql = "select distinct t.ticket_date from ACCA_UPL_IP_M t where t.source_name = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_UPL_IP_M", "ACCA_UPL_IP_M", sql);
        PageHandler pageHandler = moveComponent.createUplPageHandler(normalPool, allocateSource);
        moveComponent.executeByDate(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }

    /**
     * ACCA 国际承运日数据 状态
     */
    @RequestMapping(value = "/moveUplIpD.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_UPL_IP_D", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveUplIpD() {
        NormalPool<String[]> normalPool = moveComponent.getRefundUplPool(1000, "ACCA_UPL_IP_D");
        String sql = "select distinct t.ticket_date from ACCA_UPL_IP_D t where t.source_name = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_UPL_IP_D", "ACCA_UPL_IP_D", sql);
        PageHandler pageHandler = moveComponent.createUplPageHandler(normalPool, allocateSource);
        moveComponent.executeByDate(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }

    /**
     * ACCA 国内承运月数据 状态
     */
    @RequestMapping(value = "/moveUplDpM.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_UPL_DP_M", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveUplDpM() {
        NormalPool<String[]> normalPool = moveComponent.getRefundUplPool(1000, "ACCA_UPL_DP_M");
        String sql = "select distinct t.ticket_date from ACCA_UPL_DP_M t where t.source_name = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_UPL_DP_M", "ACCA_UPL_DP_M", sql);
        PageHandler pageHandler = moveComponent.createUplPageHandler(normalPool, allocateSource);
        moveComponent.executeByDate(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }

    /**
     * ACCA 国内承运日数据 状态
     */
    @RequestMapping(value = "/moveUplDpD.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_UPL_DP_D", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveUplDpD() {
        NormalPool<String[]> normalPool = moveComponent.getRefundUplPool(1000, "ACCA_UPL_DP_D");
        String sql = "select distinct t.ticket_date from ACCA_UPL_DP_D t where t.source_name = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_UPL_DP_D", "ACCA_UPL_DP_D", sql);
        PageHandler pageHandler = moveComponent.createUplPageHandler(normalPool, allocateSource);
        moveComponent.executeByDate(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }
}
