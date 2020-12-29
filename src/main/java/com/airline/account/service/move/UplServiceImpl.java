package com.airline.account.service.move;

import com.airline.account.mapper.acca.AUplMapper;
import com.airline.account.mapper.et.UplMapper;
import com.airline.account.model.acca.AUpl;
import com.airline.account.model.et.Upl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ydc on 2020/12/2.
 */
@Service
public class UplServiceImpl implements UplService {
    @Autowired
    private AUplMapper aUplMapper;

    @Autowired
    private UplMapper uplMapper;

    @Override
    public void moveDDpUpl(String cxr, String doc, String issueDate) {
        List<AUpl> list = aUplMapper.queryDDpUpl(cxr, doc, issueDate);
        List<Upl> upls = exchange(list, "ACCA_DP_D");
        uplMapper.insertUpl(upls);
    }

    @Override
    public void moveDIpUpl(String cxr, String doc, String issueDate) {
        List<AUpl> list = aUplMapper.queryDIpUpl(cxr, doc, issueDate);
        List<Upl> upls = exchange(list, "ACCA_IP_D");
        uplMapper.insertUpl(upls);
    }

    private List<Upl> exchange(List<AUpl> aUpls, String etSource){
        List<Upl> list = new ArrayList<>();
        for (AUpl aUpl : aUpls){
            Upl upl = new Upl();
            upl.setEtSource(etSource);
            upl.setDocumentCarrierIataNo(aUpl.getAirline3code());
            upl.setDocumentNo(aUpl.getTicketNo());
            upl.setIssueDate(aUpl.getTicketDate());
            upl.setCouponNo(aUpl.getCouponNo());
            upl.setAgentIataNo(aUpl.getAgentCode());
            upl.setFlightType(aUpl.getFlightType());
            upl.setCarrierO(aUpl.getCarrierO());
            upl.setSubCarrier(aUpl.getSubCarrier());
            upl.setFlightRounteCd(aUpl.getFlightRounteCd());
            upl.setFlightRouteType(aUpl.getFlightRouteType());
            upl.setRoundType(aUpl.getRoundType());
            upl.setFlightNo(aUpl.getFlightNo());
            upl.setCarrierDate(aUpl.getCarrierDate());
            upl.setTailNbr(aUpl.getTailNbr());
            upl.setDeptAirport(aUpl.getDeptAirport());
            upl.setArrAirport(aUpl.getArrAirport());
            upl.setMainClass(aUpl.getMainClass());
            upl.setSubClass(aUpl.getSubClass());
            upl.setFareBasis(aUpl.getFareBasis());
            upl.setSaleCurrency(aUpl.getSaleCurrency());
            upl.setProductCode(aUpl.getProductCode());
            upl.setKeyAccount(aUpl.getKeyAccount());
            upl.setEtFlag(aUpl.getEtFlag());
            upl.setFfpMemberNo(aUpl.getFfpMemberNo());
            upl.setTourCode(aUpl.getTourCode());
            upl.setChartereFlag(aUpl.getChartereFlag());
            upl.setPaxType(aUpl.getPaxType());
            upl.setPaxQty(aUpl.getPaxQty());
            upl.setLuggageHeight(aUpl.getLuggageHeight());
            upl.setGrossIncome(aUpl.getGrossIncome());
            upl.setGrossIncomeSc(aUpl.getGrossIncomeSc());
            upl.setNetIncome(aUpl.getNetIncome());
            upl.setNetIncomeSc(aUpl.getNetIncomeSc());
            upl.setAgentCommissionRate(aUpl.getAgentCommissionRate());
            upl.setAgentCommission(aUpl.getAgentCommission());
            upl.setAgentCommissionSc(aUpl.getAgentCommissionSc());
            upl.setAddedCommission(aUpl.getAddedCommission());
            upl.setAddedCommissionSc(aUpl.getAddedCommissionSc());
            upl.setBggPricedFee(aUpl.getBggPricedFee());
            upl.setBggPricedFeeSc(aUpl.getBggPricedFeeSc());
            upl.setAirportTax(aUpl.getAirportTax());
            upl.setAirportTaxSc(aUpl.getAirportTaxSc());
            upl.setFuelSurcharge(aUpl.getFuelSurcharge());
            upl.setFuelSurchargeSc(aUpl.getFuelSurchargeSc());
            upl.setAviationInsurance(aUpl.getAviationInsurance());
            upl.setAviationInsuranceSc(aUpl.getAviationInsuranceSc());
            upl.setSpaId(aUpl.getSpaId());
            upl.setMktCarrierCode(aUpl.getMktCarrierCode());
            upl.setMktFlightNo(aUpl.getMktFlightNo());
            upl.setEmdType(aUpl.getEmdType());
            upl.setEmdReasonCode(aUpl.getEmdReasonCode());
            upl.setEmdSubReasonCode(aUpl.getEmdSubReasonCode());
            upl.setNetNetIncomeCny(aUpl.getNetNetIncomeCny());
            upl.setNetNetIncomeUsd(aUpl.getNetNetIncomeUsd());
            list.add(upl);
        }
        return list;
    }
}
