package com.airline.account.utils;

import com.airline.account.model.acca.Sal;
import com.airline.account.model.et.Segment;
import com.fate.pool.normal.CascadeNormalPoolFactory;

import static com.airline.account.utils.MatchUtil.*;

/**
 * @author ydc
 * @date 2021/1/19.
 */

public final class PoolUtil implements Constant{

    public static void addSeg(CascadeNormalPoolFactory factory, Sal sal) {
        String[] status = sal.getCouponUseIndicator().split("");
        if (isNumber(sal.getCouponUseIndicator())) {
            addNumberSeg(factory, sal, status);
        } else {
            addCharSeg(factory, sal, status);
        }
    }

    /**
     * 国内航段映射
     */
    private static void addCharSeg(CascadeNormalPoolFactory factory, Sal sal, String[] status) {
        for (int i = 0, len = status.length; i < len; i++) {
            if (!STATUS_VOID.equals(status[i])) {
                if (i == 0) {
                    Segment seg = buildSeg1(sal, status[i]);
                    factory.appendObject(POOL_KEY_SEGMENT, seg);
                } else if (i == 1) {
                    Segment seg = buildSeg2(sal, status[i]);
                    factory.appendObject(POOL_KEY_SEGMENT, seg);
                } else if (i == 2) {
                    Segment seg = buildSeg3(sal, status[i]);
                    factory.appendObject(POOL_KEY_SEGMENT, seg);
                } else if (i == 3) {
                    Segment seg = buildSeg4(sal, status[i]);
                    factory.appendObject(POOL_KEY_SEGMENT, seg);
                }
            }
        }
    }

    private static void addNumberSeg(CascadeNormalPoolFactory factory, Sal sal, String[] status) {
        for (String s : status) {
            if ("1".equals(s)) {
                Segment seg = buildSeg1(sal, STATUS_VALID);
                factory.appendObject(POOL_KEY_SEGMENT, seg);
            } else if ("2".equals(s)) {
                Segment seg = buildSeg2(sal, STATUS_VALID);
                factory.appendObject(POOL_KEY_SEGMENT, seg);
            } else if ("3".equals(s)) {
                Segment seg = buildSeg3(sal, STATUS_VALID);
                factory.appendObject(POOL_KEY_SEGMENT, seg);
            } else if ("4".equals(s)) {
                Segment seg = buildSeg4(sal, STATUS_VALID);
                factory.appendObject(POOL_KEY_SEGMENT, seg);
            }
        }
    }

    private static boolean isNumber(String str) {
        return str.matches(".*[0-9]+.*");
    }
}
