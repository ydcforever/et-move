package com.airline.account.utils;

import com.airline.account.model.acca.Sal;
import com.airline.account.model.acca.TaxDp;
import com.airline.account.model.acca.TaxIp;
import com.airline.account.model.et.Segment;
import com.airline.account.model.et.Tax;
import com.airline.account.model.et.Ticket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ydc
 * @date 2020/12/29.
 */
public final class MatchUtil {

    private static final String TAX_FR = "FR";

    private static final String COUPON_VALID = "F";

    private static final String COUPON_STATUS_SALE = "S";

    /**
     * SAL票面映射
     */
    public static Ticket getTicket(Sal sal, String cnjTktString, String etSource) {
        Ticket ticket = new Ticket();
        if (sal.getCnjNo() == 1) {
            ticket.setConjunctionTicketString(cnjTktString);
        }
        ticket.setDocumentCarrierIataNo(sal.getAirline3code());
        ticket.setDocumentNo(sal.getTicketNo());
        ticket.setIssueDate(sal.getIssueDate());
        ticket.setDocumentType(sal.getSaleType());
        ticket.setAgentIataNo(sal.getAgentNo());
        ticket.setCnjCurrent(sal.getCnjNo());
        ticket.setConjunctionTicketNo(sal.getFirstTicketNo());
        ticket.setTourCode(sal.getTourCode());
        ticket.setCommissionRate(EtFormat.numberFormat(sal.getAgentCommissionRate()));
        ticket.setCommissionAmount(EtFormat.numberFormat(sal.getAgentCommission()));
        ticket.setEndorsementRestriction(sal.getEndorsementRestriction());
        ticket.setEquivalentFarePaid(EtFormat.numberFormat(sal.getIncomePaxSc()));
        ticket.setEquivalentFareCurrencyCode(sal.getSaleCurrency());
        ticket.setTotalFare(sal.getIncomePaySc());
        ticket.setTotalFareCurrency(sal.getSaleCurrency());
        ticket.setPaxType(EtFormat.psgTypeFormat(sal.getPaxType()));
        ticket.setPaxQty(sal.getPaxQty());
        ticket.setFareCalculationArea(sal.getFca());
        ticket.setPassengerName(sal.getPaxName());
        ticket.setDataSource(sal.getDataSource());
        ticket.setEtSource(etSource);
        ticket.setPnrNo(sal.getPnrNo());
        ticket.setGpFlag(sal.getGpFlag());
        ticket.setAutoTicketFlag(sal.getAutoTicketFlag());
        return ticket;
    }

    /**
     * 连票号串
     */
    public static String getCnjTktString(List<Sal> list) {
        StringBuilder cnjTktString = new StringBuilder();
        int i = 0;
        for (Sal sal : list) {
            if (sal.getCnjNo() > 1) {
                if(i > 0) {
                    cnjTktString.append(";");
                }
                cnjTktString.append(sal.getTicketNo());
                i++;
            }
        }
        return cnjTktString.toString();
    }

    /**
     * 第一航段
     */
    public static Segment buildSeg1(Sal sal, String couponUse) {
        Segment seg = new Segment();
        fillNormalSeg(seg, sal, couponUse);
        seg.setCouponNo(1);
        seg.setOriginCityCode(sal.getAirport1());
        seg.setDestinationCityCode(sal.getAirport2());
        if (COUPON_VALID.equals(couponUse)) {
            seg.setCarrierIataNo(sal.getCarrier1());
            seg.setFareBasis(sal.getFareBasis1());
            seg.setFlightNo(sal.getFlightNo1());
            seg.setFlightDate(EtFormat.fltDateFormat(sal.getFlightDt1()));
            seg.setServiceClass(sal.getSubClass1());
        }
        return seg;
    }

    /**
     * 第二航段
     */
    public static Segment buildSeg2(Sal sal, String couponUse) {
        Segment seg = new Segment();
        fillNormalSeg(seg, sal, couponUse);
        seg.setCouponNo(2);
        seg.setOriginCityCode(sal.getAirport2());
        seg.setDestinationCityCode(sal.getAirport3());
        if (COUPON_VALID.equals(couponUse)) {
            seg.setCarrierIataNo(sal.getCarrier2());
            seg.setFareBasis(sal.getFareBasis2());
            seg.setFlightNo(sal.getFlightNo2());
            seg.setFlightDate(EtFormat.fltDateFormat(sal.getFlightDt2()));
            seg.setServiceClass(sal.getSubClass2());
        }
        return seg;
    }

    /**
     * 第三航段
     */
    public static Segment buildSeg3(Sal sal, String couponUse) {
        Segment seg = new Segment();
        fillNormalSeg(seg, sal, couponUse);
        seg.setCouponNo(3);
        seg.setOriginCityCode(sal.getAirport3());
        seg.setDestinationCityCode(sal.getAirport4());
        if (COUPON_VALID.equals(couponUse)) {
            seg.setCarrierIataNo(sal.getCarrier3());
            seg.setFareBasis(sal.getFareBasis3());
            seg.setFlightNo(sal.getFlightNo3());
            seg.setFlightDate(EtFormat.fltDateFormat(sal.getFlightDt3()));
            seg.setServiceClass(sal.getSubClass3());
        }
        return seg;
    }

    /**
     * 第四航段
     */
    public static Segment buildSeg4(Sal sal, String couponUse) {
        Segment seg = new Segment();
        fillNormalSeg(seg, sal, couponUse);
        seg.setCouponNo(4);
        seg.setOriginCityCode(sal.getAirport4());
        seg.setDestinationCityCode(sal.getAirport5());
        if (COUPON_VALID.equals(couponUse)) {
            seg.setCarrierIataNo(sal.getCarrier4());
            seg.setFareBasis(sal.getFareBasis4());
            seg.setFlightNo(sal.getFlightNo4());
            seg.setFlightDate(EtFormat.fltDateFormat(sal.getFlightDt4()));
            seg.setServiceClass(sal.getSubClass4());
        }
        return seg;
    }

    private static void fillNormalSeg(Segment seg, Sal sal, String couponUse) {
        seg.setDocumentCarrierIataNo(sal.getAirline3code());
        seg.setDocumentNo(sal.getTicketNo());
        seg.setDocumentType(sal.getSaleType());
        seg.setConjunctionTicketNo(sal.getFirstTicketNo());
        seg.setCnjCurrent(sal.getCnjNo());
        seg.setIssueDate(sal.getIssueDate());
        seg.setCouponStatusIndicator(COUPON_STATUS_SALE);
        seg.setValidCouponFlag(couponUse);
    }

    /**
     * 国际税费映射
     */
    public static List<Tax> getIpTax(List<TaxIp> taxIps, String issueDate) {
        List<Tax> taxes = new ArrayList<>();
        Map<String, Tax> map = new HashMap<>(taxIps.size());
        for (TaxIp ip : taxIps) {
            Tax tax = new Tax();
            tax.setIssueDate(issueDate);
            tax.setDocumentCarrierIataNo(ip.getAirline3code());
            tax.setDocumentNo(ip.getTicketNo());
            tax.setDocumentType(ip.getSaleType());
            tax.setTaxAmount(ip.getTaxAmount());
            tax.setTaxCurrency(ip.getCurrency());
            String taxCode = EtFormat.taxCodeFormat(ip.getTaxType());
            tax.setTaxCode(taxCode);
            if (TAX_FR.equals(taxCode)) {
                tax.setTaxSeqNo(taxes.size() + 1);
                taxes.add(tax);
            } else {
                Tax tax1 = map.get(taxCode);
                if (tax1 == null) {
                    tax.setTaxSeqNo(1);
                    map.put(taxCode, tax);
                } else {
                    tax1.setTaxAmount(tax1.getTaxAmount() + ip.getTaxAmount());
                }
            }
        }
        taxes.addAll(map.values());
        return taxes;
    }

    /**
     * 国内税费映射
     */
    public static List<Tax> getDpTax(List<TaxDp> taxDps, String issueDate) {
        List<Tax> taxes = new ArrayList<>();
        for (TaxDp dp : taxDps) {
            if (dp.getTaxAmount1() != 0) {
                Tax tax = new Tax();
                fillDpTaxNormal(tax, dp, issueDate);
                tax.setTaxCode(dp.getTaxType());
                tax.setTaxAmount(dp.getTaxAmount1());
                taxes.add(tax);
            }

            if (dp.getTaxAmount2() != 0) {
                Tax tax = new Tax();
                fillDpTaxNormal(tax, dp, issueDate);
                tax.setTaxCode(dp.getTaxType2());
                tax.setTaxAmount(dp.getTaxAmount2());
                taxes.add(tax);
            }

            if (dp.getTaxAmount3() != 0) {
                Tax tax = new Tax();
                fillDpTaxNormal(tax, dp, issueDate);
                tax.setTaxCode(dp.getTaxType3());
                tax.setTaxAmount(dp.getTaxAmount3());
                taxes.add(tax);
            }

            if (dp.getTaxAmount4() != 0) {
                Tax tax = new Tax();
                fillDpTaxNormal(tax, dp, issueDate);
                tax.setTaxCode(dp.getTaxType4());
                tax.setTaxAmount(dp.getTaxAmount4());
                taxes.add(tax);
            }

            if (dp.getTaxAmount5() != 0) {
                Tax tax = new Tax();
                fillDpTaxNormal(tax, dp, issueDate);
                tax.setTaxCode(dp.getTaxType5());
                tax.setTaxAmount(dp.getTaxAmount5());
                taxes.add(tax);
            }
        }
        return taxes;
    }

    private static void fillDpTaxNormal(Tax tax, TaxDp dp, String issueDate) {
        tax.setDocumentCarrierIataNo(dp.getAirline3code());
        tax.setDocumentNo(dp.getTicketNo());
        tax.setDocumentType(dp.getSaleType());
        tax.setIssueDate(issueDate);
        tax.setTaxCurrency(dp.getCurrency());
    }
}
