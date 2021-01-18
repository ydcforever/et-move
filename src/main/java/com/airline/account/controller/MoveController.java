package com.airline.account.controller;

import com.airline.account.model.et.Relation;
import com.airline.account.service.acca.*;
import com.airline.account.service.move.LoadSourceService;
import com.airline.account.service.move.MoveService;
import com.airline.account.utils.AllocateSource;
import com.airline.account.utils.SingleMoveComponent;
import com.fate.piece.PageHandler;
import com.fate.pool.normal.CascadeSingleFactory;
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

//    @Autowired
//    private MoveComponent moveComponent;

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
    private RfdService rfdService;

    @Autowired
    private IwbService iwbService;

    @Autowired
    private RefService refService;

    @Autowired
    private UplService uplService;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private LoadSourceService loadSourceService;

    /**
     * ACCA 国际销售日数据 含有国际改签 状态
     */
    @RequestMapping(value = "/moveDIp.do", method = RequestMethod.POST)
    @ResponseBody
    @SteerableSchedule(id = "ACCA_SAL_IP_D", cron = "0 0 0 * * ?")
    public void moveDip() {
//        CascadeNormalPoolFactory poolFactory = moveComponent.getSalPoolFactory(1000, 1);
        CascadeSingleFactory poolFactory = moveComponent.getSalPoolFactory();
        String sql = "select distinct t.issue_date from ACCA_SAL_IP_D t where t.source_name = ? order by t.issue_date";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_IP_D", "ACCA_SAL_IP_D", sql);
        PageHandler pageHandler = moveComponent.createSalPageHandler(poolFactory, allocateSource, moveDipService);
        loadSourceService.executeByDate(poolFactory, allocateSource, pageHandler);
        poolFactory.destroy();
    }

    /**
     * ACCA 国内销售日数据 含有国际改签 状态
     */
    @RequestMapping(value = "/moveDDp.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_SAL_DP_D", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveDdp() {
//        CascadeNormalPoolFactory poolFactory = moveComponent.getSalPoolFactory(1000, 1);
        CascadeSingleFactory poolFactory = moveComponent.getSalPoolFactory();
        String sql = "select distinct t.issue_date from ACCA_SAL_DP_D t where t.source_name = ? order by t.issue_date";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_DP_D", "ACCA_SAL_DP_D", sql);
        PageHandler pageHandler = moveComponent.createSalPageHandler(poolFactory, allocateSource, moveDdpService);
        loadSourceService.executeByDate(poolFactory, allocateSource, pageHandler);
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
//        CascadeNormalPoolFactory poolFactory = moveComponent.getSalPoolFactory(1000, 1);
        CascadeSingleFactory poolFactory = moveComponent.getSalPoolFactory();
        String sql = "select distinct t.issue_date from ACCA_SAL_IP_M t where t.source_name = ? order by t.issue_date";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_IP_M", "ACCA_SAL_IP_M", sql);
        PageHandler pageHandler = moveComponent.createSalPageHandler(poolFactory, allocateSource, moveMipService);
        loadSourceService.executeByDate(poolFactory, allocateSource, pageHandler);
        poolFactory.destroy();
    }

    /**
     * ACCA 国内销售月数据 含有国际改签 状态
     */
    @RequestMapping(value = "/moveMDp.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_SAL_DP_M", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveMdp() {
//        CascadeNormalPoolFactory poolFactory = moveComponent.getSalPoolFactory(1000, 1);
        CascadeSingleFactory poolFactory = moveComponent.getSalPoolFactory();
        String sql = "select distinct t.issue_date from ACCA_SAL_DP_M t where t.source_name = ? order by t.issue_date";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_DP_M", "ACCA_SAL_DP_M", sql);
        PageHandler pageHandler = moveComponent.createSalPageHandler(poolFactory, allocateSource, moveMdpService);
        loadSourceService.executeByDate(poolFactory, allocateSource, pageHandler);
        poolFactory.destroy();
    }

    /**
     * 无文件提供
     * ACCA 国内改签 状态
     */
    @RequestMapping(value = "/moveRfdDp.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_RFD_DP_M", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveRfdDp() {
        NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "ACCA_RFD_DP_M");
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_RFD_DP_M", "ACCA_RFD_DP_M", "");
        PageHandler pageHandler = rfdService.createPageHandler(normalPool, allocateSource);
        loadSourceService.executeByFile(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }

    /**
     * 国际改签 状态
     */
    @RequestMapping(value = "/moveExchange.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "Exchange", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveExchange() {
        NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "Exchange");
        String sql = "select distinct t.issue_date from ITAX_AUDITOR_EXCHANGE t where t.ori_source = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ITAX_AUDITOR_EXCHANGE", "Exchange", sql);
        PageHandler pageHandler = exchangeService.createPageHandler(normalPool, allocateSource);
        loadSourceService.executeByDate(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }

    /**
     * 国际退票 状态
     */
    @RequestMapping(value = "/moveRefund.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "Refund", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveRefund() {
        NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "Refund");
        String sql = "select distinct t.issue_date from ITAX_AUDITOR_REFUND t where t.ori_source = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ITAX_AUDITOR_REFUND", "Refund", sql);
        PageHandler pageHandler = refundService.createPageHandler(normalPool, allocateSource);
        loadSourceService.executeByDate(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }

    /**
     * ACCA 国内退票 状态
     */
    @RequestMapping(value = "/moveRefDp.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "moveRefDp", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveRefDp() {
        NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "ACCA_REF_DP_M");
        String sql = "select distinct t.refund_date from ACCA_REF_DP_M t where t.source_name = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_REF_DP_M", "ACCA_REF_DP_M", sql);
        PageHandler pageHandler = refService.createPageHandler(normalPool, allocateSource);
        loadSourceService.executeByDate(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }

    /**
     * ACCA 国际承运月数据 状态
     */
    @RequestMapping(value = "/moveUplIpM.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_UPL_IP_M", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveUplIpM() {
        NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "ACCA_UPL_IP_M");
        String sql = "select distinct t.ticket_date from ACCA_UPL_IP_M t where t.source_name = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_UPL_IP_M", "ACCA_UPL_IP_M", sql);
        PageHandler pageHandler = uplService.createPageHandler(normalPool, allocateSource);
        loadSourceService.executeByDate(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }

    /**
     * ACCA 国际承运日数据 状态
     */
    @RequestMapping(value = "/moveUplIpD.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_UPL_IP_D", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveUplIpD() {
        NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "ACCA_UPL_IP_D");
        String sql = "select distinct t.ticket_date from ACCA_UPL_IP_D t where t.source_name = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_UPL_IP_D", "ACCA_UPL_IP_D", sql);
        PageHandler pageHandler = uplService.createPageHandler(normalPool, allocateSource);
        loadSourceService.executeByDate(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }

    /**
     * ACCA 国内承运月数据 状态
     */
    @RequestMapping(value = "/moveUplDpM.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_UPL_DP_M", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveUplDpM() {
        NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "ACCA_UPL_DP_M");
        String sql = "select distinct t.ticket_date from ACCA_UPL_DP_M t where t.source_name = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_UPL_DP_M", "ACCA_UPL_DP_M", sql);
        PageHandler pageHandler = uplService.createPageHandler(normalPool, allocateSource);
        loadSourceService.executeByDate(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }

    /**
     * ACCA 国内承运日数据 状态
     */
    @RequestMapping(value = "/moveUplDpD.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_UPL_DP_D", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveUplDpD() {
        NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "ACCA_UPL_DP_D");
        String sql = "select distinct t.ticket_date from ACCA_UPL_DP_D t where t.source_name = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_UPL_DP_D", "ACCA_UPL_DP_D", sql);
        PageHandler pageHandler = uplService.createPageHandler(normalPool, allocateSource);
        loadSourceService.executeByDate(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }

    /**
     * ACCA 对内开账国内 月数据
     */
    @RequestMapping(value = "/moveIwbDpM.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_IWB_DP_M", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveIwbDpM(){
        NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "ACCA_IWB_DP_M");
        String sql = "select distinct t.ticket_date from ACCA_IWB_DP_M t where t.source_name = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_IWB_DP_M", "ACCA_IWB_DP_M", sql);
        PageHandler pageHandler = iwbService.createPageHandler(normalPool, allocateSource);
        loadSourceService.executeByDate(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }

    /**
     * ACCA 对内开账国内 月数据
     */
    @RequestMapping(value = "/moveIwbIpM.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_IWB_IP_M", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveIwbIpM(){
        NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "ACCA_IWB_IP_M");
        String sql = "select distinct t.ticket_date from ACCA_IWB_IP_M t where t.source_name = ? ";
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_IWB_IP_M", "ACCA_IWB_IP_M", sql);
        PageHandler pageHandler = iwbService.createPageHandler(normalPool, allocateSource);
        loadSourceService.executeByDate(normalPool, allocateSource, pageHandler);
        normalPool.destroy();
    }
}
