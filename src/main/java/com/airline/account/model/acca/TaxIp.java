package com.airline.account.model.acca;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ydc
 * @date 2020/9/15
 */
@Data
@NoArgsConstructor
public class TaxIp {

    private String dataType;

    private String outputSys;

    private String balMonth;

    private String balPeriod;

    private String airline3code;

    private String ticketNo;

    private String saleType;

    private String currency;

    private String taxType;

    private double taxAmountCny;

    private double taxAmount;

    public TaxIp(String taxType, double taxAmount) {
        this.taxType = taxType;
        this.taxAmount = taxAmount;
    }
}
