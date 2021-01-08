package com.airline.account.mapper.et;

import com.airline.account.model.et.EtUpl;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ydc
 * @date 2020/12/2
 */
@Repository
public interface EtUplMapper {

    /**
     * 插入承运信息
     * @param etUpl 承运
     */
    void insertUpl(EtUpl etUpl);

    /**
     * 更新承运信息
     *
     * @param etUpl 承运
     */
    void updateUpl(EtUpl etUpl);
}
