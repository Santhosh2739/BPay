package ycash.wallet.json.pojo.registration;

import java.io.Serializable;

import ycash.wallet.json.pojo.generic.GenericRequest;
import ycash.wallet.json.pojo.generic.TransType;

public class CustomerActivationRequest extends GenericRequest implements Serializable {

    private static final long serialVersionUID = -6351977117167548295L;
    private String oldPin;
    private String newPin;
    private String mobileNumber;
    private String typeofscreen;


    public String getTypeofscreen() {
        return typeofscreen;
    }

    public void setTypeofscreen(String typeofscreen) {
        this.typeofscreen = typeofscreen;
    }

    public String getOldPin() {
        return oldPin;
    }

    public void setOldPin(String oldPin) {
        this.oldPin = oldPin;
    }

    public String getNewPin() {
        return newPin;
    }

    public void setNewPin(String newPin) {
        this.newPin = newPin;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public CustomerActivationRequest() {
        super.setG_transType(TransType.REGISTER_ACTIVATION.name());
    }


}
