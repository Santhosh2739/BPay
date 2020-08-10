package ycash.wallet.json.pojo.sendmoney;

import java.util.List;

import ycash.wallet.json.pojo.generic.GenericResponse;


public class P2PReceiverNumberListResponse extends GenericResponse {

	private List<String> mobileNumber;

	public List<String> getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(List<String> mobileNumber) {
		this.mobileNumber = mobileNumber;
	}


}
