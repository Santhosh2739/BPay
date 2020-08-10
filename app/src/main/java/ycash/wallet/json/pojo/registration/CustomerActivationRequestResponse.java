package ycash.wallet.json.pojo.registration;

import java.io.Serializable;
import java.math.BigDecimal;

import ycash.wallet.json.pojo.generic.GenericResponse;

public class CustomerActivationRequestResponse extends GenericResponse implements Serializable {
	
	private static final long serialVersionUID = 1832859276676989814L;


	


	private String newPin;
	private String mobileNumber;
	private String enc_key;
	private String mac_key;
	private String custFirstName;
	private Double walletBalance;
	private String custLastName;
	
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

	
	public String getCustLastName() {
		return custLastName;
	}

	public void setCustLastName(String custLastName) {
		this.custLastName = custLastName;
	}


	
}
