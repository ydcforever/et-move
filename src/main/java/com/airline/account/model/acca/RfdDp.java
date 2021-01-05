package com.airline.account.model.acca;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ydc
 * @date 2020-12-30
 */
@Data
@NoArgsConstructor
public class RfdDp {

    private String dataType;

    private String outputSys;

    private String balMonth;

    private String balPeriod;

    private String transferTicketCompany;

    private String transferTicketNo;

    private Integer couponNo;

    private String agentNo;

    private String ticketType;

    private String saleType;

    private String transferNewTicketCompany;

    private String transferNewTicketNo;

    private String sourceName;

}