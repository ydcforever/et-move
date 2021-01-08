package com.airline.account.model.et;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ydc
 * @date 2021/1/6.
 */
@Data
@NoArgsConstructor
public class CouponStatus {

    private String documentCarrierIataNo;

    private String documentNo;

    private String issueDate;

    private Integer couponNo;

    private String status;

    public CouponStatus(String documentCarrierIataNo, String documentNo, Integer couponNo, String status) {
        this.documentCarrierIataNo = documentCarrierIataNo;
        this.documentNo = documentNo;
        this.couponNo = couponNo;
        this.status = status;
    }
}
