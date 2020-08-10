package ycash.wallet.json.pojo.mobilebilloperator;

import java.math.BigDecimal;

import ycash.wallet.json.pojo.generic.GenericResponse;

/**
 * Created by 10037 on 6/20/2017.
 */

public class DomesticRechargeTranHistoryResponsePojo extends GenericResponse {
    private String transactionId;
    private long serverTime;
    private String operatorName;
    private String recipientMobileNumber;
    private String rechargeAmt;
    private double denominationinKWD;
    private String paymentRefId;
    private double senderbalanceAfeter;

    private BigDecimal totalRedemptionPoints;

    public BigDecimal getTotalRedemptionPoints() {
        return totalRedemptionPoints;
    }

    public void setTotalRedemptionPoints(BigDecimal totalRedemptionPoints) {
        this.totalRedemptionPoints = totalRedemptionPoints;
    }


    public String getBeforeCashBackPoints() {
        return beforeCashBackPoints;
    }

    public void setBeforeCashBackPoints(String beforeCashBackPoints) {
        this.beforeCashBackPoints = beforeCashBackPoints;
    }

    public String getAfterCashBackPoints() {
        return afterCashBackPoints;
    }

    public void setAfterCashBackPoints(String afterCashBackPoints) {
        this.afterCashBackPoints = afterCashBackPoints;
    }

    private String tranType;
    private String rechargeCode;
    private String serialNo;
    private String billType;
    private Byte paymentStatus;

    private String beforeCashBackPoints;
    private String afterCashBackPoints;

//    mobile no:
//    CashbackAmt
//    Date:
//
//    Before balance:
//    Afterbalance
//          Remainingpoints

    //offers
    private Long offerId;
    private double discountPrice;

    //newly added
    private String branch;

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    private String offerDescription;

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    private double totalPrice;

    public Byte getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Byte paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getRechargeCode() {
        return rechargeCode;
    }

    public void setRechargeCode(String rechargeCode) {
        this.rechargeCode = rechargeCode;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getRecipientMobileNumber() {
        return recipientMobileNumber;
    }

    public void setRecipientMobileNumber(String recipientMobileNumber) {
        this.recipientMobileNumber = recipientMobileNumber;
    }

    public String getRechargeAmt() {
        return rechargeAmt;
    }

    public void setRechargeAmt(String rechargeAmt) {
        this.rechargeAmt = rechargeAmt;
    }

    public double getDenominationinKWD() {
        return denominationinKWD;
    }

    public void setDenominationinKWD(double denominationinKWD) {
        this.denominationinKWD = denominationinKWD;
    }

    public String getPaymentRefId() {
        return paymentRefId;
    }

    public void setPaymentRefId(String paymentRefId) {
        this.paymentRefId = paymentRefId;
    }

    public double getSenderbalanceAfeter() {
        return senderbalanceAfeter;
    }

    public void setSenderbalanceAfeter(double senderbalanceAfeter) {
        this.senderbalanceAfeter = senderbalanceAfeter;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
