package com.airline.account.model.et;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Ticket {

    private String documentCarrierIataNo;

    private String documentNo;

    private String issueDate;

    private String documentType;

    private String conjunctionTicketIndicator;

    private String conjunctionTicketString;

    private String conjunctionTicketNo;

    private Integer cnjCurrent;

    private Integer cnjTotal;

    private Integer salesReportNo;

    private Integer documentCheckDigit;

    private String auditorCouponSource;

    private String auditorCouponType;

    private String transactionCode;

    private Integer admAcmNo;

    private String agentIataNo;

    private String couponUseIndicator;

    private String tourCode;

    private String internationalSaleIndicator;

    private double miscellaneousFeeAmount;

    private String miscellaneousFeeCurrencyCd;

    private double commissionRate;

    private double commissionAmount;

    private String commissionCurrencyCode;

    private String vatOnCommissionFlag;

    private Integer vatCountryId;

    private double vatOnCommissionAmount;

    private String originalIssueCarrierIataNo;

    private String originalIssueDocumentNo;

    private String originalIssueDate;

    private String originalIssueAgentNo;

    private String originalIssuePlace;

    private String endorsementRestriction;

    private double fare;

    private String fareCurrencyCode;

    private double equivalentFarePaid;

    private String equivalentFareCurrencyCode;

    private String totalFare;

    private String totalFareCurrency;

    private String fareCalculationArea;

    private String passengerName;

    private String personalOrDutyIndicator;

    private String employeeId;

    private String formOfPaymentCode;

    private String formOfPaymentType;

    private String creditCardNo;

    private Integer salesReportPostStatusNo;

    private double fareToNucRateOfExchange;

    private double eqvFarePaidFareExchange;

    private double eqvFarePaidBaseExchange;

    private double ourValueFare;

    private String refundExchangeUnmatchInd;

    private String fareCheckMethodIndicator;

    private String prorationIndicator;

    private String taxAllocationIndicator;

    private String loadTransactionCode;

    private String additionalCollectionFlag;

    private String voluntaryReroutingFlag;

    private String journeyApplicableIndicator;

    private String journey;

    private Integer journeyCode;

    private double residualValue;

    private String mcoUsedIndicator;

    private String statOiDocumentCarrIataNo;

    private String statOiDocumentNo;

    private String statOiPlace;

    private String statOiDate;

    private String statOiAgentIataNo;

    private double expectedValueOfMco;

    private String dataSource;

    private String officeId;

    private String nationFlag;

    private String gdsCode;

    private String itFlag;

    private String ffNo;

    private String paxType;

    private String paxQty;

    private String manualFlag;

    private String etSource;

    private String pnrNo;

    private String gpFlag;

    private String autoTicketFlag;

    public Ticket(String documentCarrierIataNo, String documentNo, String documentType, String issueDate) {
        this.documentCarrierIataNo = documentCarrierIataNo;
        this.documentNo = documentNo;
        this.documentType = documentType;
        this.issueDate = issueDate;
    }
}