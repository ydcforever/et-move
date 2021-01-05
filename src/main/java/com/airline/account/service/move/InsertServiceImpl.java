package com.airline.account.service.move;

import com.airline.account.mapper.et.SegmentMapper;
import com.airline.account.mapper.et.TaxMapper;
import com.airline.account.mapper.et.TicketMapper;
import com.airline.account.mapper.et.UplMapper;
import com.airline.account.model.et.Segment;
import com.airline.account.model.et.Tax;
import com.airline.account.model.et.Ticket;
import com.airline.account.model.et.Upl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * @author ydc
 * @date 2021/1/5.
 */
@Service
public class InsertServiceImpl implements InsertService {

    @Autowired
    private UplMapper uplMapper;

    @Autowired
    private TicketMapper ticketMapper;

    @Autowired
    private SegmentMapper segmentMapper;

    @Autowired
    private TaxMapper taxMapper;

    @Override
    public void insertUplWithUpdate(Upl upl) throws Exception{
        try{
            uplMapper.insertUpl(upl);
        } catch (DuplicateKeyException e){
            uplMapper.updateUpl(upl);
        }
    }

    @Override
    public void insertTicketWithUpdate(Ticket ticket) throws Exception{
        try{
            ticketMapper.insertTicket(ticket);
        } catch (DuplicateKeyException e){
            ticketMapper.updateTicket(ticket);
        }
    }

    @Override
    public void insertSegmentWithUpdate(Segment segment) throws Exception{
        try{
            segmentMapper.insertSegment(segment);
        } catch (DuplicateKeyException e){
            segmentMapper.updateSegment(segment);
        }
    }

    @Override
    public void insertTaxWithUpdate(Tax tax) throws Exception{
        try{
            taxMapper.insertTax(tax);
        } catch (DuplicateKeyException e) {
            taxMapper.updateTax(tax);
        }
    }
}
