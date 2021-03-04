package com.airline.account.service.allocate;

import com.airline.account.model.allocate.AllocateSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author ydc
 * @date 2021/2/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ThreadLogServiceImplTest {

    @Autowired
    private ThreadLogService threadLogService;

    @Test
    public void insertLog() {
        AllocateSource allocateSource = new AllocateSource(100, "ACCA_SAL_DP_M");
        allocateSource.setFileName("M_DP_SAL_202011_20201211.csv");
        allocateSource.setIssueDate("20200804");
        allocateSource.setHasExecute("N");
        allocateSource.beginPiece("1");

        threadLogService.insertLog(allocateSource);
        allocateSource.finishPiece("Y");
        threadLogService.updateLog(allocateSource);
    }
}