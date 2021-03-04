package com.airline.account.mapper.allocate;

import com.airline.account.model.allocate.Work;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author ydc
 * @date 2021/2/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class WorkMapperTest {

    @Autowired
    private WorkMapper workMapper;

    @Test
    public void query() {
        List<Work> list = workMapper.query("ACCA_SAL_DP_M");
        System.out.println(list);
    }
}