package com.airline.account.service.move;

import com.airline.account.model.et.Segment;
import com.airline.account.model.et.Tax;
import com.airline.account.model.et.Ticket;
import org.springframework.stereotype.Service;

/**
 * @author ydc
 * @date 2021/1/5.
 */
@Service
public interface InsertService {

    /**
     * 票面插入
     *
     * @param ticket 票面
     * @throws Exception e
     */
    void insertTicketWithUpdate(Ticket ticket) throws Exception;

    /**
     * 航段插入
     *
     * @param segment 航段
     * @throws Exception e
     */
    void insertSegmentWithUpdate(Segment segment) throws Exception;

    /**
     * 税费插入
     *
     * @param tax 税费
     * @throws Exception e
     */
    void insertTaxWithUpdate(Tax tax) throws Exception;

}
