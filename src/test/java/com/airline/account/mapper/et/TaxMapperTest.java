package com.airline.account.mapper.et;

import com.airline.account.model.et.Tax;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author ydc
 * @date 2020/12/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TaxMapperTest {

    @Autowired
    private TaxMapper taxMapper;

    @Test
    public void updateTax() {
        Tax tax = new Tax("781", "2319871615", "20190329", "YQ", 4);
        tax.setTaxSeqNo(1);
        taxMapper.updateTax(tax);
    }
}