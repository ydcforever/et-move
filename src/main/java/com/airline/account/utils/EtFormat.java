package com.airline.account.utils;

import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;

/**
 *
 * @author ydc
 * @date 2020/11/29
 */
public final class EtFormat implements Constant{

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

    private static String dateFormat(String date, String... parsePatterns) {
        try {
            DateUtils.parseDate(date, parsePatterns);
            return date;
        } catch (ParseException e) {
            return "30001230";
        }
    }
}
