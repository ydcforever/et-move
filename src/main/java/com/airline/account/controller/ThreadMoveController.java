package com.airline.account.controller;

import com.airline.account.model.allocate.AllocateSource;
import com.airline.account.model.et.Relation;
import com.airline.account.service.acca.*;
import com.airline.account.service.allocate.StatisticService;
import com.airline.account.service.allocate.ThreadLogService;
import com.airline.account.service.allocate.WorkService;
import com.airline.account.service.move.MoveService;
import com.airline.account.utils.AllocateRunnable;
import com.airline.account.utils.Handler;
import com.airline.account.utils.SingleMoveComponent;
import com.fate.piece.PageHandler;
import com.fate.pool.normal.CascadeNormalPoolFactory;
import com.fate.pool.normal.NormalPool;
import com.fate.schedule.SteerableSchedule;
import com.fate.thread.ResourcePoolUtil;
import com.fate.thread.ResourceRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author ydc
 * @date 2021/2/23.
 */
@RestController
@RequestMapping("/et")
public class ThreadMoveController {

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
    private StatisticService statisticService;

    @Autowired
    private ThreadLogService threadLogService;

    @Autowired
    private WorkService workService;

    /**
     * ACCA 国际销售日数据 含有国际改签 状态
     */
    @RequestMapping(value = "/moveDIp.do", method = RequestMethod.POST)
    @ResponseBody
    @SteerableSchedule(id = "ACCA_SAL_IP_D", cron = "0 0 0 * * ?")
    public void moveDip() {
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_IP_D");
        moveSal(allocateSource, moveDipService);
    }

    /**
     * ACCA 国内销售日数据 含有国际改签 状态
     */
    @RequestMapping(value = "/moveDDp.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_SAL_DP_D", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveDdp() {
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_DP_D");
        moveSal(allocateSource, moveDdpService);
    }

    /**
     * ACCA 国际销售月数据 含有国际改签 状态
     */
    @RequestMapping(value = "/moveMIp.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_SAL_IP_M", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveMip() {
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_IP_M");
        moveSal(allocateSource, moveMipService);
    }

    /**
     * ACCA 国内销售月数据 含有国际改签 状态
     */
    @RequestMapping(value = "/moveMDp.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_SAL_DP_M", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveMdp() {
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_SAL_DP_M");
        moveSal(allocateSource, moveMdpService);
    }

    private void moveSal(AllocateSource allocateSource, MoveService moveService){
        LinkedBlockingQueue<AllocateSource> queue = statisticService.getResource(allocateSource);
        ThreadPoolExecutor executor = ResourcePoolUtil.createThreadPool(queue, new ResourceRunnable<AllocateSource>() {
            @Override
            public Runnable generate(LinkedBlockingQueue<AllocateSource> resource, int sequence) {
                CascadeNormalPoolFactory poolFactory = moveComponent.getSalPoolFactory();
                return new AllocateRunnable(queue, String.valueOf(sequence), threadLogService, poolFactory,
                        new Handler() {
                    @Override
                    public PageHandler generate(AllocateSource allocateSource) {
                        return moveComponent.createSalPageHandler(poolFactory, allocateSource, moveService);
                    }
                });
            }
        }, 4, 60);
        ResourcePoolUtil.shutdown(executor, 600);
        workService.updateWorkRunning(allocateSource.getTableName());
    }

    /**
     * 国际改签 状态
     */
    @RequestMapping(value = "/moveExchange.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "Exchange", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveExchange() {
        AllocateSource allocateSource = new AllocateSource(10000, "ITAX_AUDITOR_EXCHANGE");
        LinkedBlockingQueue<AllocateSource> queue = statisticService.getResource(allocateSource);
        ThreadPoolExecutor executor = ResourcePoolUtil.createThreadPool(queue, new ResourceRunnable<AllocateSource>() {
            @Override
            public Runnable generate(LinkedBlockingQueue<AllocateSource> resource, int sequence) {
                NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "Exchange");
                return new AllocateRunnable(queue, String.valueOf(sequence), threadLogService, normalPool, new Handler() {
                    @Override
                    public PageHandler generate(AllocateSource allocateSource) {
                        return exchangeService.createPageHandler(normalPool, allocateSource);
                    }
                });
            }
        }, 4, 60);
        ResourcePoolUtil.shutdown(executor, 600);
        workService.updateWorkRunning(allocateSource.getTableName());
    }

    /**
     * 国际退票 状态
     */
    @RequestMapping(value = "/moveRefund.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "Refund", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveRefund() {
        AllocateSource allocateSource = new AllocateSource(10000, "ITAX_AUDITOR_REFUND");
        LinkedBlockingQueue<AllocateSource> queue = statisticService.getResource(allocateSource);
        ThreadPoolExecutor executor = ResourcePoolUtil.createThreadPool(queue, new ResourceRunnable<AllocateSource>() {
            @Override
            public Runnable generate(LinkedBlockingQueue<AllocateSource> resource, int sequence) {
                NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "Refund");
                return new AllocateRunnable(queue, String.valueOf(sequence), threadLogService, normalPool, new Handler() {
                    @Override
                    public PageHandler generate(AllocateSource allocateSource) {
                        return refundService.createPageHandler(normalPool, allocateSource);
                    }
                });
            }
        }, 4, 60);
        ResourcePoolUtil.shutdown(executor, 600);
        workService.updateWorkRunning(allocateSource.getTableName());
    }

    /**
     * ACCA 国内退票 状态
     */
    @RequestMapping(value = "/moveRefDp.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_REF_DP_M", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveRefDp() {
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_REF_DP_M");
        allocateSource.setDateColName("REFUND_DATE");
        LinkedBlockingQueue<AllocateSource> queue = statisticService.getResource(allocateSource);
        ThreadPoolExecutor executor = ResourcePoolUtil.createThreadPool(queue, new ResourceRunnable<AllocateSource>() {
            @Override
            public Runnable generate(LinkedBlockingQueue<AllocateSource> resource, int sequence) {
                NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "ACCA_REF_DP_M");
                return new AllocateRunnable(queue, String.valueOf(sequence), threadLogService, normalPool, new Handler() {
                    @Override
                    public PageHandler generate(AllocateSource allocateSource) {
                        return refService.createPageHandler(normalPool, allocateSource);
                    }
                });
            }
        }, 4, 60);
        ResourcePoolUtil.shutdown(executor, 600);
        workService.updateWorkRunning(allocateSource.getTableName());
    }

    /**
     * ACCA 国际承运月数据 状态
     */
    @RequestMapping(value = "/moveUplIpM.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_UPL_IP_M", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveUplIpM() {
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_UPL_IP_M");
        allocateSource.setDateColName("TICKET_DATE");
        LinkedBlockingQueue<AllocateSource> queue = statisticService.getResource(allocateSource);
        ThreadPoolExecutor executor = ResourcePoolUtil.createThreadPool(queue, new ResourceRunnable<AllocateSource>() {
            @Override
            public Runnable generate(LinkedBlockingQueue<AllocateSource> resource, int sequence) {
                NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "ACCA_UPL_IP_M");
                return new AllocateRunnable(queue, String.valueOf(sequence), threadLogService, normalPool, new Handler() {
                    @Override
                    public PageHandler generate(AllocateSource allocateSource) {
                        return uplService.createPageHandler(normalPool, allocateSource);
                    }
                });
            }
        }, 4, 60);
        ResourcePoolUtil.shutdown(executor, 600);
        workService.updateWorkRunning(allocateSource.getTableName());
    }

    /**
     * ACCA 国际承运日数据 状态
     */
    @RequestMapping(value = "/moveUplIpD.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_UPL_IP_D", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveUplIpD() {
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_UPL_IP_D");
        allocateSource.setDateColName("TICKET_DATE");
        LinkedBlockingQueue<AllocateSource> queue = statisticService.getResource(allocateSource);
        ThreadPoolExecutor executor = ResourcePoolUtil.createThreadPool(queue, new ResourceRunnable<AllocateSource>() {
            @Override
            public Runnable generate(LinkedBlockingQueue<AllocateSource> resource, int sequence) {
                NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "ACCA_UPL_IP_D");
                return new AllocateRunnable(queue, String.valueOf(sequence), threadLogService, normalPool, new Handler() {
                    @Override
                    public PageHandler generate(AllocateSource allocateSource) {
                        return uplService.createPageHandler(normalPool, allocateSource);
                    }
                });
            }
        }, 4, 60);
        ResourcePoolUtil.shutdown(executor, 600);
        workService.updateWorkRunning(allocateSource.getTableName());
    }

    /**
     * ACCA 国内承运月数据 状态
     */
    @RequestMapping(value = "/moveUplDpM.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_UPL_DP_M", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveUplDpM() {
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_UPL_DP_M");
        allocateSource.setDateColName("TICKET_DATE");
        LinkedBlockingQueue<AllocateSource> queue = statisticService.getResource(allocateSource);
        ThreadPoolExecutor executor = ResourcePoolUtil.createThreadPool(queue, new ResourceRunnable<AllocateSource>() {
            @Override
            public Runnable generate(LinkedBlockingQueue<AllocateSource> resource, int sequence) {
                NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "ACCA_UPL_DP_M");
                return new AllocateRunnable(queue, String.valueOf(sequence), threadLogService, normalPool, new Handler() {
                    @Override
                    public PageHandler generate(AllocateSource allocateSource) {
                        return uplService.createPageHandler(normalPool, allocateSource);
                    }
                });
            }
        }, 4, 60);
        ResourcePoolUtil.shutdown(executor, 600);
        workService.updateWorkRunning(allocateSource.getTableName());
    }

    /**
     * ACCA 国内承运日数据 状态
     */
    @RequestMapping(value = "/moveUplDpD.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_UPL_DP_D", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveUplDpD() {
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_UPL_DP_D");
        allocateSource.setDateColName("TICKET_DATE");
        LinkedBlockingQueue<AllocateSource> queue = statisticService.getResource(allocateSource);
        ThreadPoolExecutor executor = ResourcePoolUtil.createThreadPool(queue, new ResourceRunnable<AllocateSource>() {
            @Override
            public Runnable generate(LinkedBlockingQueue<AllocateSource> resource, int sequence) {
                NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "ACCA_UPL_DP_D");
                return new AllocateRunnable(queue, String.valueOf(sequence), threadLogService, normalPool, new Handler() {
                    @Override
                    public PageHandler generate(AllocateSource allocateSource) {
                        return uplService.createPageHandler(normalPool, allocateSource);
                    }
                });
            }
        }, 4, 60);
        ResourcePoolUtil.shutdown(executor, 600);
        workService.updateWorkRunning(allocateSource.getTableName());
    }

    /**
     * ACCA 对内开账国内 月数据
     */
    @RequestMapping(value = "/moveIwbDpM.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_IWB_DP_M", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveIwbDpM(){
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_IWB_DP_M");
        allocateSource.setDateColName("TICKET_DATE");
        LinkedBlockingQueue<AllocateSource> queue = statisticService.getResource(allocateSource);
        ThreadPoolExecutor executor = ResourcePoolUtil.createThreadPool(queue, new ResourceRunnable<AllocateSource>() {
            @Override
            public Runnable generate(LinkedBlockingQueue<AllocateSource> resource, int sequence) {
                NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "ACCA_IWB_DP_M");
                return new AllocateRunnable(queue, String.valueOf(sequence), threadLogService, normalPool, new Handler() {
                    @Override
                    public PageHandler generate(AllocateSource allocateSource) {
                        return iwbService.createPageHandler(normalPool, allocateSource);
                    }
                });
            }
        }, 4, 60);
        ResourcePoolUtil.shutdown(executor, 600);
        workService.updateWorkRunning(allocateSource.getTableName());
    }

    /**
     * ACCA 对内开账国内 月数据
     */
    @RequestMapping(value = "/moveIwbIpM.do", method = RequestMethod.POST)
    @SteerableSchedule(id = "ACCA_IWB_IP_M", cron = "0 0 0 * * ?")
    @ResponseBody
    public void moveIwbIpM(){
        AllocateSource allocateSource = new AllocateSource(10000, "ACCA_IWB_IP_M");
        allocateSource.setDateColName("TICKET_DATE");
        LinkedBlockingQueue<AllocateSource> queue = statisticService.getResource(allocateSource);
        ThreadPoolExecutor executor = ResourcePoolUtil.createThreadPool(queue, new ResourceRunnable<AllocateSource>() {
            @Override
            public Runnable generate(LinkedBlockingQueue<AllocateSource> resource, int sequence) {
                NormalPool<Relation> normalPool = moveComponent.getRelationPool(1000, "ACCA_IWB_IP_M");
                return new AllocateRunnable(queue, String.valueOf(sequence), threadLogService, normalPool, new Handler() {
                    @Override
                    public PageHandler generate(AllocateSource allocateSource) {
                        return iwbService.createPageHandler(normalPool, allocateSource);
                    }
                });
            }
        }, 4, 60);
        ResourcePoolUtil.shutdown(executor, 600);
        workService.updateWorkRunning(allocateSource.getTableName());
    }
}
