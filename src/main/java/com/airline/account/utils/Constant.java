package com.airline.account.utils;

/**
 * @author ydc
 * @date 2020/12/30.
 */
public interface Constant {

    String ERROR_IWB2UPL = "MOVE_IWB2UPL";

    String ERROR_IWB_STATUS = "UPDATE_IWB_STATUS";

    String ERROR_UPL2ET = "MOVE_UPL2ET";

    String ERROR_REFUND2ET = "MOVE_REFUND2ET";

    String ERROR_REF_DP2ET = "MOVE_REF_DP2ET";

    String ERROR_REF_STATUS = "UPDATE_REF_STATUS";

    String ERROR_EXCHANGE2ET = "MOVE_EXCHANGE2ET";

    String ERROR_EXCHANGE_STATUS = "UPDATE_EXCHANGE_STATUS";

    String ERROR_SAL = "MOVE_SAL";

    String SYS_DP = "DP";

    /**
     * 票面对象池
     */
    String POOL_KEY_TICKET = "TICKET";

    /**
     * 航段对象池
     */
    String POOL_KEY_SEGMENT = "SEGMENT";

    /**
     * 税费对象池
     */
    String POOL_KEY_TAX = "TAX";

    /**
     * 改签关系对象池
     */
    String POOL_KEY_EXCHANGE = "EXCHANGE";

    /**
     * 成人
     */
    String PSG_A = "A";

    /**
     * 儿童
     */
    String PSG_C = "C";

    /**
     * 婴儿
     */
    String PSG_I = "I";

    /**
     * 改签
     */
    String STATUS_EXCHANGE = "E";

    /**
     * 退票
     */
    String STATUS_REFUND = "R";

    /**
     * Flown
     */
    String STATUS_FLOWN = "F";

    /**
     * Inward Billing
     */
    String STATUS_IWB = "I";

    /**
     * 有效
     */
    String STATUS_VALID = "F";

    /**
     * 无效
     */
    String STATUS_VOID = "V";

    /**
     * 无效
     */
    String COUPON_INVALID = "0";

    /**
     * open for use
     */
    String STATUS_SALE = "S";

    /**
     * FR税
     */
    String TAX_FR = "FR";

    /**
     * XF税
     */
    String TAX_XF = "XF";
}
