package com.airline.account.model.acca;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ydc
 * @date 2020/12/18
 */
@Data
@NoArgsConstructor
public class Refund {

    private String primaryTicketNo;

    /**
     * 换开序号
     */
    private String cnjNo;

    /**
     *  退票独有
     */
    private String ticketNoStr;

    private String issueDate;

    private String orgTicketNo;

    /**
     *  退票是4联分号隔开，改签一联
     */
    private String couponStatus;

    private String orgIssueDate;
}
