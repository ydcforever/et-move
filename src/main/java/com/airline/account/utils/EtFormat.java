package com.airline.account.utils;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author ydc
 * @date 2020/11/29
 */
public final class EtFormat implements Constant{

    private static final String DATE_SPACER = "-";

    public static String taxCodeFormat(String taxCode) {
        if(taxCode.contains(TAX_XF)){
            return TAX_XF;
        } else {
            return taxCode;
        }
    }

    public static String psgTypeFormat(String psg) {
        if (PSG_A.equals(psg)) {
            return "ADT";
        } else if (PSG_C.equals(psg)) {
            return "CHD";
        } else if (PSG_I.equals(psg)) {
            return "INF";
        }
        return psg;
    }

    public static double numberFormat(String str){
        try {
           return new Double(str.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public static int intFormat(String str){
        try {
            return Integer.parseInt(str.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public static String fltDateFormat(String date) {
        return "99999999".equals(date) ? "30001230" : dateFormat(date, "yyyyMMdd");
    }

    public static String dateFormat(String date, String targetFormat) {
        try {
            String[] a = new String[1];
            if(date.contains(DATE_SPACER)) {
                a[0] = "yyyy" + DATE_SPACER + "MM" + DATE_SPACER + "dd";
            } else {
                a[0] = "yyyyMMdd";
            }
            Date dt = DateUtils.parseDate(date, a);
            return DateFormatUtils.format(dt, targetFormat);
        } catch (Exception e) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(3000, Calendar.DECEMBER, 31);
            return DateFormatUtils.format(calendar, targetFormat);
        }
    }
}
