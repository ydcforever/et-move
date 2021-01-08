package com.airline.account.model.et;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ydc
 * @date 2020/12/2
 */
@Data
@NoArgsConstructor
public class EtUpl {
    private String documentCarrierIataNo;

    private String documentNo;

    private String issueDate;

    private Integer couponNo;

    private String documentType;

    private String agentIataNo;

    private String flightType;

    private String carrierO;

    private String subCarrier;

    private String flightRounteCd;

    private String flightRouteType;

    private String roundType;

    private String flightNo;

    private String carrierDate;

    private String tailNbr;

    private String deptAirport;

    private String arrAirport;

    private String mainClass;

    private String subClass;

    private String fareBasis;

    private String saleCurrency;

    private String productCode;

    private String keyAccount;

    private String etFlag;

    private String ffpMemberNo;

    private String tourCode;

    private String chartereFlag;

    private String paxType;

    private Integer paxQty;

    private double luggageHeight;

    private double grossIncome;

    private double grossIncomeSc;

    private double netIncome;

    private double netIncomeSc;

    private double agentCommissionRate;

    private double agentCommission;

    private double agentCommissionSc;

    private double addedCommission;

    private double addedCommissionSc;

    private double bggPricedFee;

    private double bggPricedFeeSc;

    private double airportTax;

    private double airportTaxSc;

    private double fuelSurcharge;

    private double fuelSurchargeSc;

    private double aviationInsurance;

    private double aviationInsuranceSc;

    private String spaId;

    private String mktCarrierCode;

    private String mktFlightNo;

    private String emdType;

    private String emdReasonCode;

    private String emdSubReasonCode;

    private double netNetIncomeCny;

    private double netNetIncomeUsd;

    private String etSource;
}
