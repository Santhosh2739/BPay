package ycash.wallet.json.pojo.virtualprepaidcards;

import java.util.List;

import ycash.wallet.json.pojo.generic.GenericResponse;

/**
 * Created by 10037 on 7/12/2017.
 */

public class TopupValidationResponse extends GenericResponse {
    private String Response;
    private String ResponseDescription;
    private String PaymentID;
    private String PaymentRef;
    private String Date;

    private String recieverMobileNumber;
    private String transferAmount;
    private String balanceAfter;
    private String customerId;
    private String transactionId;
    private String txnDate;
    private List<String> txnList;
    private String operatorName;
    private String qty;

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }


    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getResponseDescription() {
        return ResponseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        ResponseDescription = responseDescription;
    }

    public String getPaymentID() {
        return PaymentID;
    }

    public void setPaymentID(String paymentID) {
        PaymentID = paymentID;
    }

    public String getPaymentRef() {
        return PaymentRef;
    }

    public void setPaymentRef(String paymentRef) {
        PaymentRef = paymentRef;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getRecieverMobileNumber() {
        return recieverMobileNumber;
    }

    public void setRecieverMobileNumber(String recieverMobileNumber) {
        this.recieverMobileNumber = recieverMobileNumber;
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(String balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }

    public List<String> getTxnList() {
        return txnList;
    }

    public void setTxnList(List<String> txnList) {
        this.txnList = txnList;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
}
