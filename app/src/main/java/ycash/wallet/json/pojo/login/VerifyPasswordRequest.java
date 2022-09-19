package ycash.wallet.json.pojo.login;

public class VerifyPasswordRequest extends GenericRequest{
	private String oldPin;
    private String mobileNumber;

	public String getOldPin() {
		return oldPin;
	}

	public void setOldPin(String oldPin) {
		this.oldPin = oldPin;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
}
