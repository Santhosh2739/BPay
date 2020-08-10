package ycash.wallet.json.pojo.wheretopay;

import ycash.wallet.json.pojo.generic.GenericRequest;

/**
 * Created by 30099 on 2/19/2016.
 */
public class ImageLogoRequest extends GenericRequest {
    private String merchantRefNumber;

    public String getMerchantRefNumber() {
        return merchantRefNumber;
    }

    public void setMerchantRefNumber(String merchantRefNumber) {
        this.merchantRefNumber = merchantRefNumber;
    }
}
