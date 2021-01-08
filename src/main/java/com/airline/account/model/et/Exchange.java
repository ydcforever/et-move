package com.airline.account.model.et;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ydc
 * @date 2020/6/17
 */
@Data
@NoArgsConstructor
public class Exchange {
    private String root;

    private String chain;

    private String lev;

    private String documentCarrierIataNo;

    private String documentNo;

    private String documentType;

    private String issueInExchCarrierIataNo;

    private String issueInExchDocumentNo;

    private String issueInExchDocumentType;

    private String exchangeCouponUseIndicator;

    private String issueDate;

    private String issueInExchDate;
}
