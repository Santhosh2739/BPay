package ycash.wallet.json.pojo.virtualprepaidcards;

import java.math.BigDecimal;

import ycash.wallet.json.pojo.generic.GenericResponse;

/**
 * Created by 10037 on 7/12/2017.
 */

public class VocherL2Response extends GenericResponse {
    private String Response;
    private String OperatorName;
    private Double Denomination;
    private String SerialNo;
    private String RechargeCode;
    private String Password;
    private String DateTime;
    private String txnRefNumber;


    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getOperatorName() {
        return OperatorName;
    }

    public void setOperatorName(String operatorName) {
        OperatorName = operatorName;
    }

    public Double getDenomination() {
        return Denomination;
    }

    public void setDenomination(Double denomination) {
        Denomination = denomination;
    }

    public String getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(String serialNo) {
        SerialNo = serialNo;
    }

    public String getRechargeCode() {
        return RechargeCode;
    }

    public void setRechargeCode(String rechargeCode) {
        RechargeCode = rechargeCode;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getTxnRefNumber() {
        return txnRefNumber;
    }

    public void setTxnRefNumber(String txnRefNumber) {
        this.txnRefNumber = txnRefNumber;
    }
}
