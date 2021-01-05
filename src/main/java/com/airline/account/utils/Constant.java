package com.airline.account.utils;

/**
 * @author ydc
 * @date 2020/12/30.
 */
public interface Constant {

    String ERROR_UPL = "UPL";

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
