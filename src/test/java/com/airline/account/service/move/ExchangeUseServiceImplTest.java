package com.airline.account.service.move;

import com.airline.account.model.et.Relation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author ydc
 * @date 2021/1/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ExchangeUseServiceImplTest {

    @Autowired
    private ExchangeUseService exchangeUseService;

    @Test
    public void insertExchangeWithUpdate() {
        Relation exchange = new Relation();
        exchange.setDocumentCarrierIataNo("781");
        exchange.setDocumentNo("23457");
        exchange.setIssueDate("2020-01-01");
        exchange.setOperateDocumentCarrierIataNo("781");
        exchange.setOperateDocumentNo("2894");
        exchange.setOperateIssueDate("2020-01-02");
        exchange.setCouponUseIndicator("1240");
        exchangeUseService.insertExchangeWithUpdate("exchange", exchange);
    }
}