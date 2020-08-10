package ycash.wallet.json.pojo.login;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.translimit.TransactionLimitResponse;


public class CustomerLoginRequestReponse extends GenericResponse {
    private String enc_key;
    private String mac_key;
    private String mobileNumber;
    private String custFirstName;
    private ArrayList<TransactionLimitResponse> transferLimits;
    private Double walletBalance;
    private String oauth_2_0_client_token;
    private String uniqueCustomerId;
    private String custLastName;
    private HashMap<String, TransactionLimitResponse> filteredLimits = null;
    private String g_servertime;

    private long lastSuccessLoginTime;
    private String domesticRechargeVersion;
    private int dueInvoiceCount;
    private ArrayList<String> bannerDetails;

    public ArrayList<String> getBannerDetails() {
        return bannerDetails;
    }

    public void setBannerDetails(ArrayList<String> bannerDetails) {
        this.bannerDetails = bannerDetails;
    }

    public String getDomesticRechargeVersion() {
        return domesticRechargeVersion;
    }

    public void setDomesticRechargeVersion(String domesticRechargeVersion) {
        this.domesticRechargeVersion = domesticRechargeVersion;
    }


    public long getLastSuccessLoginTime() {
        return lastSuccessLoginTime;
    }

    public void setLastSuccessLoginTime(long lastSuccessLoginTime) {
        this.lastSuccessLoginTime = lastSuccessLoginTime;
    }


    public String getG_servertime() {
        return g_servertime;
    }

    public void setG_servertime(String g_servertime) {
        this.g_servertime = g_servertime;
    }

    public HashMap<String, TransactionLimitResponse> getFilteredLimits() {
        return filteredLimits;
    }

    public void processFilteringOfLimitsInHashMap() {
        if (transferLimits != null && transferLimits.size() > 0) {
            filteredLimits = new HashMap<String, TransactionLimitResponse>();
            for (TransactionLimitResponse limits : transferLimits) {
                if (!filteredLimits.containsKey(limits.getSubTranType())) {
                    filteredLimits.put(limits.getSubTranType(), limits);
                } else {
                    TransactionLimitResponse prev = filteredLimits.get(limits.getSubTranType());
                    if (prev.getTpinLimit() == 0.00 && limits.getTpinLimit() > 0) {
                        prev.setTpinLimit(limits.getTpinLimit());
                    } else if (limits.getTpinLimit() == 0 && prev.getTpinLimit() > 0) {
                        prev.setMaxValuePerTransaction(limits.getMaxValuePerTransaction());
                        prev.setMinValuePerTransaction(limits.getMinValuePerTransaction());
                    }
                }
            }
        }
    }

    public String getCustLastName() {
        return custLastName;
    }

    public void setCustLastName(String custLastName) {
        this.custLastName = custLastName;
    }

    public ArrayList<TransactionLimitResponse> getTransferLimits() {
        return transferLimits;
    }

    public void setTransferLimits(ArrayList<TransactionLimitResponse> transferLimits) {
        this.transferLimits = transferLimits;
    }

    public String getUniqueCustomerId() {
        return uniqueCustomerId;
    }

    public void setUniqueCustomerId(String uniqueCustomerId) {
        this.uniqueCustomerId = uniqueCustomerId;
    }

    public String getEnc_key() {
        return enc_key;
    }

    public void setEnc_key(String enc_key) {
        this.enc_key = enc_key;
    }

    public String getMac_key() {
        return mac_key;
    }

    public void setMac_key(String mac_key) {
        this.mac_key = mac_key;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCustFirstName() {
        return custFirstName;
    }

    public void setCustFirstName(String custFirstName) {
        this.custFirstName = custFirstName;
    }

    public Double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(Double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public String getOauth_2_0_client_token() {
        return oauth_2_0_client_token;
    }

    public void setOauth_2_0_client_token(String oauth_2_0_client_token) {
        this.oauth_2_0_client_token = oauth_2_0_client_token;
    }

    public int getDueInvoiceCount() {
        return dueInvoiceCount;
    }

    public void setDueInvoiceCount(int dueInvoiceCount) {
        this.dueInvoiceCount = dueInvoiceCount;
    }
}
