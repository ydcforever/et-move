package com.airline.account.mapper.et;

import com.airline.account.model.et.Segment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author ydc
 * @date 2020/12/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class SegmentMapperTest {

    @Autowired
    private SegmentMapper segmentMapper;

    @Test
    public void updateSegment() {
        Segment segment = new Segment("781", "2311549526", 1);
        segment.setIssueDate("20190329");
        segment.setOriginCityCode("SVO");
        segment.setDestinationCityCode("PVG");
        segment.setCarrierIataNo("FM");
        segmentMapper.updateSegment(segment);
    }
}