package com.airline.account.service.move;

import com.airline.account.model.et.CouponStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ydc
 * @date 2021/1/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class BatchServiceImplTest {

    @Autowired
    private BatchService batchService;

    @Test
    public void updateSegmentStatus() throws Exception {
        List<CouponStatus> a = new ArrayList<>();
        a.add(new CouponStatus("781", "2382395439", 3, "E"));
        batchService.updateSegmentStatus(a);
    }
}