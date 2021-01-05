package com.airline.account.mapper.et;

import com.airline.account.model.et.Segment;
import org.springframework.stereotype.Repository;

/**
 * @author ydc
 * @date 2020/12/31.
 */
@Repository
public interface SegmentMapper {

    /**
     * 插入航段
     *
     * @param segment 航段
     */
    void insertSegment(Segment segment);

    /**
     * 更新航段
     *
     * @param segment 航段
     */
    void updateSegment(Segment segment);
}
