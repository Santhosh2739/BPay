package ycash.wallet.json.pojo.changes;


import ycash.wallet.json.pojo.login.GenericRequest;

/**
 * Created by 30099 on 1/25/2016.
 */
public class UpdateCustMobilenumberRequest extends GenericRequest {
    String oldmobilenumber;
    String newmobilenumber;
    String civilid;
    String walletpin;
    String deviceid;
    public String getOldmobilenumber() {
        return oldmobilenumber;
    }

    public void setOldmobilenumber(String oldmobilenumber) {
        this.oldmobilenumber = oldmobilenumber;
    }

    public String getNewmobilenumber() {
        return newmobilenumber;
    }

    public void setNewmobilenumber(String newmobilenumber) {
        this.newmobilenumber = newmobilenumber;
    }

    public String getCivilid() {
        return civilid;
    }

    public void setCivilid(String civilid) {
        this.civilid = civilid;
    }

    public String getWalletPin() {
        return walletpin;
    }

    public void setWalletPin(String walletPin) {
        this.walletpin = walletPin;
    }

    public String getDeviceId() {
        return deviceid;
    }

    public void setDeviceId(String deviceId) {
        this.deviceid = deviceId;
    }


}
