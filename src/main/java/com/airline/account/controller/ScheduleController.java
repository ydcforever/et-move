package com.airline.account.controller;

import com.fate.schedule.SteerableScheduleManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author ydc
 * @date 2019/12/10
 */
@Api(value = "定时任务管理")
@RestController
@RequestMapping("/job")
public class ScheduleController {

    @Autowired
    private SteerableScheduleManager manager;

    @ApiOperation(value = "开启所有定时")
    @RequestMapping(value = "/startAll.do", method = RequestMethod.POST)
    @ResponseBody
    public void start() {
        manager.startSchedule();
    }

    @ApiOperation(value = "停止定时")
    @RequestMapping(value = "/stop.do", method = RequestMethod.POST)
    @ResponseBody
    public void stop(@RequestParam("id")String id) {
        manager.stopJob(id, true);
    }

    @ApiOperation(value = "开启定时")
    @RequestMapping(value = "/start.do", method = RequestMethod.POST)
    @ResponseBody
    public void start(@RequestParam("id")String id) {
        manager.startJob(id);
    }

    @ApiOperation(value = "修改定时时间")
    @RequestMapping(value = "/modify.do", method = RequestMethod.POST)
    @ResponseBody
    public void modify(@RequestParam("id")String id, @RequestParam("cron") String cron) {
        manager.modifyJob(id, cron);
    }
}
