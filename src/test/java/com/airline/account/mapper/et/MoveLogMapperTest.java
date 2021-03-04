package com.airline.account.mapper.et;

import com.airline.account.model.et.MoveLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author ydc
 * @date 2021/1/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class MoveLogMapperTest {

    @Autowired
    private MoveLogMapper moveLogMapper;

    @Test
    public void insertLog() {
        MoveLog moveLog = new MoveLog("ww", "ss", "error");
        moveLogMapper.insertLog(moveLog);
    }
}