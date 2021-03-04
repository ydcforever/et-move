package com.airline.account.service.move;

import com.airline.account.model.acca.Sal;
import com.airline.account.model.et.Relation;
import com.airline.account.model.et.Tax;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author ydc
 * @date 2020/9/17
 */
@Service
public interface MoveService {

    /**
     * 连票
     *
     * @param primarySal 主票
     * @return 票面信息
     */
    List<Sal> getSal(Sal primarySal);

    /**
     * 查询票面税费明细
     *
     * @param primarySal 主票
     * @return 税费明细
     */
    List<Tax> getTax(Sal primarySal);

    /**
     * 查询改签关系
     * 国内使用票面
     *
     * @param primarySal 主票
     * @return 改签关系
     */
    List<Relation> getExchange(Sal primarySal);
}
