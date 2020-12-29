package com.airline.account.mapper.acca;

import com.airline.account.model.acca.Sal;
import com.airline.account.model.acca.TaxDp;
import com.airline.account.model.acca.TaxIp;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author ydc
 * @date 2020/9/17
 */
@Repository
public interface TaxDiMapper {

    /**
     * 国内日数据 税费
     *
     * @param primarySal 主票
     * @return list of dp tax
     */
    List<TaxDp> queryDdpTax(Sal primarySal);

    /**
     * 国内月数据 税费
     *
     * @param primarySal 主票
     * @return list of dp tax
     */
    List<TaxDp> queryMdpTax(Sal primarySal);

    /**
     * 国际日数据 税费
     *
     * @param primarySal 主票
     * @return list of ip tax
     */
    List<TaxIp> queryDipTax(Sal primarySal);

    /**
     * 国际月数据 税费
     *
     * @param primarySal 主票
     * @return list of ip tax
     */
    List<TaxIp> queryMipTax(Sal primarySal);

}
