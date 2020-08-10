package ycash.wallet.json.pojo.Internationaltopup;

import java.math.BigDecimal;

import ycash.wallet.json.pojo.generic.GenericResponse;


public class InternationalRechargeInitiationResponsePojo  extends GenericResponse{


    private String recipientMobile;
    private Double denominationAmt;
    private Double denominationAmtinKWD;
    private String customerID;
    private String transactionID;
    private long serverTime;
    private String status;
    private Double walletbalanceafter;
    private String currencyName;


    private BigDecimal totalRedemptionPoints;

    public BigDecimal getTotalRedemptionPoints() {
        return totalRedemptionPoints;
    }

    public void setTotalRedemptionPoints(BigDecimal totalRedemptionPoints) {
        this.totalRedemptionPoints = totalRedemptionPoints;
    }


    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Double getWalletbalanceafter() {
        return walletbalanceafter;
    }

    public void setWalletbalanceafter(Double walletbalanceafter) {
        this.walletbalanceafter = walletbalanceafter;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getRecipientMobile() {
        return recipientMobile;
    }
    public void setRecipientMobile(String recipientMobile) {
        this.recipientMobile = recipientMobile;
    }

    public Double getDenominationAmt() {
        return denominationAmt;
    }

    public void setDenominationAmt(Double denominationAmt) {
        this.denominationAmt = denominationAmt;
    }

    public Double getDenominationAmtinKWD() {
        return denominationAmtinKWD;
    }

    public void setDenominationAmtinKWD(Double denominationAmtinKWD) {
        this.denominationAmtinKWD = denominationAmtinKWD;
    }

    public String getCustomerID() {
        return customerID;
    }
    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }
    public String getTransactionID() {
        return transactionID;
    }
    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }
    public long getServerTime() {
        return serverTime;
    }
    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

}
