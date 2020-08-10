package ycash.wallet.json.pojo.activationresponse;

public class ActivationResponsePojo {

    private String newPin;
    private String mobileNumber;
    private String encKey;
    private String macKey;
    private String custFirstName;
    private Double walletBalance;
    private Integer gStatus;
    private String gStatusDescription;
    private String gErrorDescription;
    private String gResponseTransType;

    /**
     *
     * @return
     * The newPin
     */
    public String getNewPin() {
        return newPin;
    }

    /**
     *
     * @param newPin
     * The newPin
     */
    public void setNewPin(String newPin) {
        this.newPin = newPin;
    }

    /**
     *
     * @return
     * The mobileNumber
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     *
     * @param mobileNumber
     * The mobileNumber
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    /**
     *
     * @return
     * The encKey
     */
    public String getEncKey() {
        return encKey;
    }

    /**
     *
     * @param encKey
     * The enc_key
     */
    public void setEncKey(String encKey) {
        this.encKey = encKey;
    }

    /**
     *
     * @return
     * The macKey
     */
    public String getMacKey() {
        return macKey;
    }

    /**
     *
     * @param macKey
     * The mac_key
     */
    public void setMacKey(String macKey) {
        this.macKey = macKey;
    }

    /**
     *
     * @return
     * The custFirstName
     */
    public String getCustFirstName() {
        return custFirstName;
    }

    /**
     *
     * @param custFirstName
     * The custFirstName
     */
    public void setCustFirstName(String custFirstName) {
        this.custFirstName = custFirstName;
    }

    /**
     *
     * @return
     * The walletBalance
     */
    public Double getWalletBalance() {
        return walletBalance;
    }

    /**
     *
     * @param walletBalance
     * The walletBalance
     */
    public void setWalletBalance(Double walletBalance) {
        this.walletBalance = walletBalance;
    }

    /**
     *
     * @return
     * The gStatus
     */
    public Integer getGStatus() {
        return gStatus;
    }

    /**
     *
     * @param gStatus
     * The g_status
     */
    public void setGStatus(Integer gStatus) {
        this.gStatus = gStatus;
    }

    /**
     *
     * @return
     * The gStatusDescription
     */
    public String getGStatusDescription() {
        return gStatusDescription;
    }

    /**
     *
     * @param gStatusDescription
     * The g_status_description
     */
    public void setGStatusDescription(String gStatusDescription) {
        this.gStatusDescription = gStatusDescription;
    }

    /**
     *
     * @return
     * The gErrorDescription
     */
    public String getGErrorDescription() {
        return gErrorDescription;
    }

    /**
     *
     * @param gErrorDescription
     * The g_errorDescription
     */
    public void setGErrorDescription(String gErrorDescription) {
        this.gErrorDescription = gErrorDescription;
    }

    /**
     *
     * @return
     * The gResponseTransType
     */
    public String getGResponseTransType() {
        return gResponseTransType;
    }

    /**
     *
     * @param gResponseTransType
     * The g_response_trans_type
     */
    public void setGResponseTransType(String gResponseTransType) {
        this.gResponseTransType = gResponseTransType;
    }

}