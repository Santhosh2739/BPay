package ycash.wallet.json.pojo.ecomotp;

public class BWebEComOTP {

    private int ecomTxnId;
    private String createdDate;

    public int getEcomTxnId() {
        return ecomTxnId;
    }

    public void setEcomTxnId(int ecomTxnId) {
        this.ecomTxnId = ecomTxnId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getMerchantTxnRefNo() {
        return merchantTxnRefNo;
    }

    public void setMerchantTxnRefNo(String merchantTxnRefNo) {
        this.merchantTxnRefNo = merchantTxnRefNo;
    }

    public String getTxnRefNo() {
        return txnRefNo;
    }

    public void setTxnRefNo(String txnRefNo) {
        this.txnRefNo = txnRefNo;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    private String merchantTxnRefNo;
    private String txnRefNo;
    private String otp;
    private String mobileNumber;


}
