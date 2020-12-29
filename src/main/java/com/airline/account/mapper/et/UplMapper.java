package com.airline.account.mapper.et;

import com.airline.account.model.et.Upl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author ydc
 * @date 2020/12/2
 */
@Repository
public interface UplMapper {

    /**
     * 承运数据插入
     * @param list l
     */
    void insertUpl(List<Upl> list);

}
