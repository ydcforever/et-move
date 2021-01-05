package com.airline.account.service.move;

import com.airline.account.model.acca.Relation;
import com.airline.account.model.et.Segment;
import com.airline.account.model.et.Tax;
import com.airline.account.model.et.Ticket;
import com.airline.account.model.et.Upl;
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

    /**
     * 更新航段状态
     *
     * @param relations list of relations
     * @throws Exception e
     */
    void updateSegmentStatus(List<String[]> relations) throws Exception;

    /**
     * 插入退票关系
     *
     * @param refunds list of refund
     * @throws Exception e
     */
    void insertRefund(List<String[]> refunds) throws Exception;

    /**
     * 插入改签关系
     *
     * @param exchanges list of exchange
     * @throws Exception e
     */
    void insertExchange(List<Relation> exchanges) throws Exception;

    /**
     * 插入承运数据
     *
     * @param uplList list of upl
     * @throws Exception e
     */
    void insertUpl(List<Upl> uplList) throws Exception;
}
