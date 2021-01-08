package com.airline.account.model.acca;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ydc
 * @date 2021/1/7.
 */
@Data
@NoArgsConstructor
public class Iwb {

    private String dataType;

    private String outputSys;

    private String balMonth;

    private String balPeriod;

    private String airline3code;

    private String ticketNo;

    private Integer couponNo;

    private String ticketType;

    private String ticketDate;

    private String agentCode;

    private String carrierCode;

    private String flightNo;

    private String flightDate;

    private String deptAirport;

    private String arrAirport;

    private String mainClass;

    private String subClass;

    private String fareBasis;

    private String saleCurrency;

    private String paxType;

    private double paxQty;

    private double luggageHeight;

    private double grossIncome;

    private double grossIncomeSc;

    private double netIncome;

    private double netIncomeSc;

    private double commissionRate;

    private double commission;

    private double commissionSc;

    private double bggPricedFee;

    private double bggPricedFeeSc;

    private double airportTax;

    private double airportTaxSc;

    private double fuelSurcharge;

    private double fuelSurchargeSc;

    private double billingGross;

    private double billingGrossSc;

    private double billingNet;

    private double billingNetSc;

    private double billingCommissionRate;

    private double billingCommission;

    private double billingCommissionSc;

    private String billingNo;

    private String billingAirline3code;

    private String billingDate;

    private String emenFlag;

    private String mktCarrierCd;

    private String mktFlightNo;

    private String saleMainClass;

    private String saleSubClass;

    private String billingCurrency;

    private String billingSource;

    private String sourceName;

}
