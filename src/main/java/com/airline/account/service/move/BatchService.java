package com.airline.account.service.move;

import com.airline.account.model.et.Segment;
import com.airline.account.model.et.Tax;
import com.airline.account.model.et.Ticket;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ydc
 * @date 2020/12/23.
 */
@Service
public interface BatchService {

    /**
     * 票面批量插入
     *
     * @param tickets list of ticket
     * @throws Exception e
     */
    void insertTicket(List<Ticket> tickets) throws Exception;

    /**
     * 航段批量插入
     *
     * @param segments list of segment
     * @throws Exception e
     */
    void insertSegment(List<Segment> segments) throws Exception;

    /**
     * 税费批量插入
     *
     * @param taxes list of tax
     * @throws Exception e
     */
    void insertTax(List<Tax> taxes) throws Exception;

}
