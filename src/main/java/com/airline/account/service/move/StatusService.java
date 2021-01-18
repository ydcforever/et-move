package com.airline.account.service.move;

import com.airline.account.model.et.Relation;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ydc
 * @date 2021/1/18.
 */
@Service
public interface StatusService {

    /**
     * 更新航段状态
     *
     * @param relations list of relations
     * @throws Exception e
     */
    void updateSegmentStatus(List<Relation> relations) throws Exception;

    /**
     * 更新航段状态
     *
     * @param relation relation
     * @throws Exception e
     */
    void updateSegmentStatus(Relation relation) throws Exception;
}
