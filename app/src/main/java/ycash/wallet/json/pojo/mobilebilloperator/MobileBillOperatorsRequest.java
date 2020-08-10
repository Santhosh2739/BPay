package ycash.wallet.json.pojo.mobilebilloperator;

import ycash.wallet.json.pojo.generic.GenericRequest;

/**
 * Created by 10037 on 6/19/2017.
 */

public class MobileBillOperatorsRequest extends GenericRequest {
    private String versionNumber;

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }


}
