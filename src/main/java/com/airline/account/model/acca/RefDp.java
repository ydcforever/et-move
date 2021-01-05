package com.airline.account.model.acca;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ydc
 * @date 2020-12-30
 */
@Data
@NoArgsConstructor
public class RefDp {

    private String dataType;

    private String outputSys;

    private String balMonth;

    private String balPeriod;

    private String refundOwner;

    private String refundType;

    private String refundOrderNo;

    private String refundOwnerCompany;

    private String refundTicketNo;

    private String refundRelationNo;

    private String refundDate;

    private String agentNo;

    private String saleCurrency;

    private double incomePay;

    private double incomePaySc;

    private double tax;

    private double taxSc;

    private double refundAmount;

    private double refundAmountSc;

    private double refundFee;

    private double refundFeeSc;

    private double admExtraFee;

    private double admExtraFeeSc;

    private String sourceName;

}