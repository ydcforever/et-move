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
public class TaxDp {

    private String dataType;

    private String outputSys;

    private String balMonth;

    private String balPeriod;

    private String airline3code;

    private String ticketNo;

    private String saleType;

    private String currency;

    private String taxType;

    private double taxAmount1Cny;

    private double taxAmount1;

    private String taxType2;

    private double taxAmount2Cny;

    private double taxAmount2;

    private String taxType3;

    private double taxAmount3Cny;

    private double taxAmount3;

    private String taxType4;

    private double taxAmount4Cny;

    private double taxAmount4;

    private String taxType5;

    private double taxAmount5Cny;

    private double taxAmount5;

//    private String sourceName;
}
