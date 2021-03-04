package com.airline.account.service.allocate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author ydc
 * @date 2021/2/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class WorkServiceTest {

    @Autowired
    private WorkService workService;

    @Test
    public void query() {
    }

    @Test
    public void preparedNewFile() {
        workService.preparedNewFile("ACCA_SAL_DP_M", "ISSUE_DATE");
    }

    @Test
    public void updateWorkRunning() {
        workService.updateWorkRunning("ACCA_SAL_DP_M");
    }
}