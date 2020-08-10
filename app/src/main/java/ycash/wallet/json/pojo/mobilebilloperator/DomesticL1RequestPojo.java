package ycash.wallet.json.pojo.mobilebilloperator;

import ycash.wallet.json.pojo.generic.GenericRequest;

/**
 * Created by 10037 on 6/20/2017.
 */

public class DomesticL1RequestPojo extends GenericRequest {


    private String operatorName;
    private String operatorType;
    private String mobileNumber;
    private String amount;
    private String billPaymentType;
    private String tpin;

    public String getDeviceIdNumber() {
        return deviceIdNumber;
    }

    public void setDeviceIdNumber(String deviceIdNumber) {
        this.deviceIdNumber = deviceIdNumber;
    }

    String deviceIdNumber;

    private String firstName;

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

    private String senderMobileNo;
    private String emailId;

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBillPaymentType() {
        return billPaymentType;
    }

    public void setBillPaymentType(String billPaymentType) {
        this.billPaymentType = billPaymentType;
    }

    public String getTpin() {
        return tpin;
    }

    public void setTpin(String tpin) {
        this.tpin = tpin;
    }
}
