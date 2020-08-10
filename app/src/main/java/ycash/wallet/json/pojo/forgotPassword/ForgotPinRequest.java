package ycash.wallet.json.pojo.forgotPassword;

import ycash.wallet.json.pojo.generic.GenericRequest;

public class ForgotPinRequest extends GenericRequest {

	private String mobileNumber;
	private String deviceID;
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	
}
