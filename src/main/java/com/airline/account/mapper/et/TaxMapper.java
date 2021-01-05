package com.airline.account.mapper.et;

import com.airline.account.model.et.Tax;
import org.springframework.stereotype.Repository;

/**
 * @author ydc
 * @date 2020/12/31.
 */
@Repository
public interface TaxMapper {

    /**
     * 插入税费
     *
     * @param tax 税费
     */
    void insertTax(Tax tax);

    /**
     * 更新税费
     *
     * @param tax 税费
     */
    void updateTax(Tax tax);
}
