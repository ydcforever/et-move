package com.airline.account.mapper.et;

import com.airline.account.model.et.Ticket;
import org.springframework.stereotype.Repository;

/**
 * @author ydc
 * @date 2020/12/31.
 */
@Repository
public interface TicketMapper {

    /**
     * 插入票面
     *
     * @param ticket 票
     */
    void insertTicket(Ticket ticket);

    /**
     * 更新票面
     *
     * @param ticket 票
     */
    void updateTicket(Ticket ticket);
}
