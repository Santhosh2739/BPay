package ycash.wallet.json.pojo.paytomerchant;


import java.math.BigDecimal;

import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;

public class PayToMerchantCommitRequestResponse extends GenericResponse {


    private BigDecimal redemptionPoints;

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    private BigDecimal totalRedemptionPoints;

    public BigDecimal getRedemptionPoints() {
        return redemptionPoints;
    }

    public void setRedemptionPoints(BigDecimal redemptionPoints) {
        this.redemptionPoints = redemptionPoints;
    }

    public BigDecimal getTotalRedemptionPoints() {
        return totalRedemptionPoints;
    }

    public void setTotalRedemptionPoints(BigDecimal totalRedemptionPoints) {
        this.totalRedemptionPoints = totalRedemptionPoints;
    }

    private String  merchantName;

    private String offerDescription;

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    private String transactionId;
    private double balance;
    private double processingfee;
    private double total;
    private long serverTime;
    private long serverTimeL2;
    private double txnAmount;

    private double discountPrice;
    private double totalPrice;
    private Long offerId;
    private String branch;

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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getProcessingfee() {
        return processingfee;
    }

    public void setProcessingfee(double processingfee) {
        this.processingfee = processingfee;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public long getServerTimeL2() {
        return serverTimeL2;
    }

    public void setServerTimeL2(long serverTimeL2) {
        this.serverTimeL2 = serverTimeL2;
    }


    public double getTxnAmount() {
        return txnAmount;
    }

    public void setTxnAmount(double txnAmount) {
        this.txnAmount = txnAmount;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientMobileNumber() {
        return recipientMobileNumber;
    }

    public void setRecipientMobileNumber(String recipientMobileNumber) {
        this.recipientMobileNumber = recipientMobileNumber;
    }

    public String getCustomerMobileNumber() {
        return customerMobileNumber;
    }

    public void setCustomerMobileNumber(String customerMobileNumber) {
        this.customerMobileNumber = customerMobileNumber;
    }

    private String recipientName;
    private String recipientMobileNumber;
    private String customerMobileNumber;

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
