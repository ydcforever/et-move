package com.airline.account.model.et;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ydc
 * @date 2020/9/7
 */
@Data
@NoArgsConstructor
public class Tax {
    private String documentCarrierIataNo;

    private String documentNo;

    private String documentType;

    private String issueDate;

    private String taxCode;

    private Integer taxSeqNo;

    private String taxAlreadyPaidFlag;

    private double taxAmount;

    private String taxCurrency;

    private String taxAllocationIndicator;

    private String invalidTaxFlag;

    public Tax(String documentCarrierIataNo, String documentNo, String issueDate, String taxCode, double taxAmount) {
        this.documentCarrierIataNo = documentCarrierIataNo;
        this.documentNo = documentNo;
        this.issueDate = issueDate;
        this.taxCode = taxCode;
        this.taxAmount = taxAmount;
    }

    public Tax taxSeqNo(Integer taxSeqNo){
        this.taxSeqNo = taxSeqNo;
        return this;
    }

    @Override
    public String toString() {
        return "Tax{" +
                "taxCode='" + taxCode + '\'' +
                ", taxSeqNo=" + taxSeqNo +
                ", taxAmount=" + taxAmount +
                '}';
    }
}
