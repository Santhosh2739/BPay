package ycash.wallet.json.pojo.changes;

import ycash.wallet.json.pojo.generic.GenericRequest;

/**
 * Created by 30099 on 1/25/2016.
 */
public class DeviceidchangeRequest extends GenericRequest {
    String mobilenumber;
    String civilid;
    String walletPin;
    String deviceid;

    public String getCivilid() {
        return civilid;
    }

    public void setCivilid(String civilid) {
        this.civilid = civilid;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getWalletPin() {
        return walletPin;
    }

    public void setWalletPin(String walletPin) {
        this.walletPin = walletPin;
    }

}
