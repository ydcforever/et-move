package com.airline.account.model.et;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author T440
 */
@Data
@NoArgsConstructor
public class Segment {

    private String documentCarrierIataNo;

    private String documentNo;

    private String issueDate;

    private String documentType;

    private Integer couponNo;

    private String conjunctionTicketNo;

    private Integer cnjCurrent;

    private String notValidBeforeDate;

    private String notValidAfterDate;

    private String originCityCode;

    private String destinationCityCode;

    private String carrierIataNo;

    private String stopoverIndicator;

    private String fareBasis;

    private String flightNo;

    private String serviceClass;

    private String flightDate;

    private String vatFlag;

    private String vatCountryId;

    private String couponStatusIndicator;

    private String couponValue;

    private String atbpNo;

    private String departureDate;

    private String arriveDate;

    private String validCouponFlag;

    private String departureTerminal;

    private String arrivalTerminal;

    private String involuntaryIndicator;

    private String compartment;

    private String baggageAllowance;

    private String emdCouponStatus;

    private String optFlightNo;

    private String asrSeat;

    private String optCarrier;

    public Segment(String documentCarrierIataNo, String documentNo, Integer couponNo) {
        this.documentCarrierIataNo = documentCarrierIataNo;
        this.documentNo = documentNo;
        this.couponNo = couponNo;
    }
}