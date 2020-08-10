package ycash.wallet.json.pojo.loadmoney;

import java.io.Serializable;

import ycash.wallet.json.pojo.generic.GenericResponse;

public class PaymentResponse extends GenericResponse implements Serializable{

	private static final long serialVersionUID = 597796318735592588L;
	private String knetUrl;
	private String errorMessage;
	
	public String getKnetUrl() {
		return knetUrl;
	}
	public void setKnetUrl(String knetUrl) {
		this.knetUrl = knetUrl;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


}
