package com.airline.account.service.acca;

import com.airline.account.mapper.acca.RelationMapper;
import com.airline.account.model.acca.AUpl;
import com.airline.account.model.acca.Relation;
import com.airline.account.model.acca.Sal;
import com.airline.account.utils.AllocateSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author ydc
 * @date 2020/12/18
 */
@Service
public class RelationServiceImpl implements RelationService {

    @Autowired
    private RelationMapper relationMapper;

    @Override
    public List<Relation> queryExchange(Sal primaryTicket) {
        return relationMapper.queryExchange(primaryTicket);
    }

    @Override
    public List<Relation> queryRefundByAllocate(AllocateSource allocateSource) {
        return relationMapper.queryRefundByAllocate(allocateSource);
    }

    @Override
    public Integer countRefundByAllocate(AllocateSource allocateSource) {
        return relationMapper.countRefundByAllocate(allocateSource);
    }

    @Override
    public List<AUpl> queryUplByAllocate(AllocateSource allocateSource) {
        return relationMapper.queryUplByAllocate(allocateSource);
    }

    @Override
    public Integer countUplByAllocate(AllocateSource allocateSource) {
        return relationMapper.countUplByAllocate(allocateSource);
    }
}
