package com.airline.account.service.move;

import com.airline.account.model.et.Relation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author ydc
 * @date 2021/1/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class RefundUseServiceImplTest {

    @Autowired
    private RefundUseService refundUseService;

    @Test
    public void insertRefundWithUpdate() {
        Relation relation = new Relation("781", "12", 1, "R");
        relation.setOperateIssueDate("2020-01-01");
        relation.setCouponUseIndicator("1235");
        refundUseService.insertRefundWithUpdate("LO", relation);
    }
}