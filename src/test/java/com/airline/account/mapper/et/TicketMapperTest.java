package com.airline.account.mapper.et;

import com.airline.account.model.et.Ticket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author ydc
 * @date 2020/12/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TicketMapperTest {

    @Autowired
    private TicketMapper ticketMapper;

    @Test
    public void updateTicket() {
        Ticket ticket = new Ticket("781", "2317715096", "N", "20190329");
        ticket.setCnjTotal(1);
        ticketMapper.updateTicket(ticket);
    }
}