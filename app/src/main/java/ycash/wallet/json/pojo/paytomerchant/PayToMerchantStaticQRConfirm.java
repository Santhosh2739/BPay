package ycash.wallet.json.pojo.paytomerchant;

import ycash.wallet.json.pojo.generic.GenericRequest;

public class PayToMerchantStaticQRConfirm extends GenericRequest {

    private String transactionId;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMerchantRefNumber() {
        return merchantRefNumber;
    }

    public void setMerchantRefNumber(String merchantRefNumber) {
        this.merchantRefNumber = merchantRefNumber;
    }


    private String merchantRefNumber;

}
