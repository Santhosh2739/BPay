package ycash.wallet.json.pojo.Internationaltopup;


import ycash.wallet.json.pojo.generic.GenericRequest;

public class InternationalRechargeRequestPojo extends GenericRequest{

    private String mobileNumber;
    private String countryName;
    private String countryCode;;
    private double denominationAmt;
    private double denominationinKWD;
    private String txnId;
    private String tpin;
    private String currencyName;


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


    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getTpin() {
        return tpin;
    }

    public void setTpin(String tpin) {
        this.tpin = tpin;
    }

    public String getTxnId() {
        return txnId;
    }
    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }
    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public String getMobileNumber() {
        return mobileNumber;
    }
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
    public String getCountryName() {
        return countryName;
    }
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    public double getDenominationAmt() {
        return denominationAmt;
    }
    public void setDenominationAmt(double denominationAmt) {
        this.denominationAmt = denominationAmt;
    }
    public double getDenominationinKWD() {
        return denominationinKWD;
    }
    public void setDenominationinKWD(double denominationinKWD) {
        this.denominationinKWD = denominationinKWD;
    }

}

