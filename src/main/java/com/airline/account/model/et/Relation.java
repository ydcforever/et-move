package com.airline.account.model.et;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ydc
 * @date 2021/1/18.
 */
@Data
@NoArgsConstructor
public class Relation {

    /**
     * root - chain - lev 树形链
     */
    private String root;

    private String chain;

    private String lev;

    private String documentCarrierIataNo;

    private String documentNo;

    /**
     * 指定格式 yyyy-MM-dd
     */
    private String issueDate;

    /**
     * operateDocumentCarrierIataNo - operateDocumentNo - operateIssueDate
     * 状态变更因子
     */
    private String operateDocumentCarrierIataNo;

    private String operateDocumentNo;

    private String operateIssueDate;

    /**
     * 退票或改签原票航段标志组
     */
    private String couponUseIndicator;

    /**
     * 退票或改签原票航段号
     */
    private Integer couponNo;

    /**
     * 退票或改签原票航段状态
     */
    private String couponStatus;

    public Relation(String operateDocumentCarrierIataNo, String operateDocumentNo, Integer couponNo, String couponStatus) {
        this.operateDocumentCarrierIataNo = operateDocumentCarrierIataNo;
        this.operateDocumentNo = operateDocumentNo;
        this.couponNo = couponNo;
        this.couponStatus = couponStatus;
    }
}
