package com.airline.account.service.acca;

import com.airline.account.mapper.acca.ExchangeMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author ydc
 * @date 2021/2/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ExchangeServiceImplTest {

    @Autowired
    private ExchangeMapper exchangeMapper;

    @Test
    public void getDpDate() {
        System.out.println(exchangeMapper.getDpExchangeDate("ACCA_SAL_DP_D","781","3637795441"));
    }
}