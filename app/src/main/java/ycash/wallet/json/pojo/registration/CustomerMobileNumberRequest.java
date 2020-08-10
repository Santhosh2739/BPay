package ycash.wallet.json.pojo.registration;

import java.io.Serializable;

import ycash.wallet.json.pojo.generic.GenericRequest;
import ycash.wallet.json.pojo.generic.TransType;


public class CustomerMobileNumberRequest extends GenericRequest implements Serializable{


    /**
     * @author soumya
     */
    private static final long serialVersionUID = -7014262751452510786L;

    private String customerMobileNumber;
    private String deviceId;
    private String deviceType;

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getCustomerMobileNumber() {
        return customerMobileNumber;
    }

    public void setCustomerMobileNumber(String customerMobileNumber) {
        this.customerMobileNumber = customerMobileNumber;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public CustomerMobileNumberRequest(){
        super.setG_transType(TransType.CHECK_BY_MOBILE_NUMBER.name());
    }

}
