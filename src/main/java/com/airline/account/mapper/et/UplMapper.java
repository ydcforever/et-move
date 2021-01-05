package com.airline.account.mapper.et;

import com.airline.account.model.et.Upl;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ydc
 * @date 2020/12/2
 */
@Repository
public interface UplMapper {

    /**
     * 插入承运信息
     * @param upl 承运
     */
    void insertUpl(Upl upl);

    /**
     * 更新承运信息
     *
     * @param upl 承运
     */
    void updateUpl(Upl upl);
}
