package ycash.wallet.json.pojo.virtualprepaidcards;

import ycash.wallet.json.pojo.generic.GenericRequest;

/**
 * Created by 10037 on 7/12/2017.
 */

public class VoucherLRequest extends GenericRequest {
    private String store;
    private String denominations;
    private String denominationsInKD;
    private String operatorName;
    private Integer quantity;
    private Double totalAmountInKD;
    private String tpin;
    private String operatorType;

    public String getDeviceIdNumber() {
        return deviceIdNumber;
    }

    public void setDeviceIdNumber(String deviceIdNumber) {
        this.deviceIdNumber = deviceIdNumber;
    }

    String deviceIdNumber;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSenderMobileNo() {
        return senderMobileNo;
    }

    public void setSenderMobileNo(String senderMobileNo) {
        this.senderMobileNo = senderMobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    private String firstName;
    private String senderMobileNo;
    private String emailId;

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getDenominations() {
        return denominations;
    }

    public void setDenominations(String denominations) {
        this.denominations = denominations;
    }

    public String getDenominationsInKD() {
        return denominationsInKD;
    }

    public void setDenominationsInKD(String denominationsInKD) {
        this.denominationsInKD = denominationsInKD;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalAmountInKD() {
        return totalAmountInKD;
    }

    public void setTotalAmountInKD(Double totalAmountInKD) {
        this.totalAmountInKD = totalAmountInKD;
    }

    public String getTpin() {
        return tpin;
    }

    public void setTpin(String tpin) {
        this.tpin = tpin;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }
}
