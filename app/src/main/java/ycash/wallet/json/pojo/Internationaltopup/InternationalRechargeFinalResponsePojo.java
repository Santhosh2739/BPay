package ycash.wallet.json.pojo.Internationaltopup;

import java.util.List;

import ycash.wallet.json.pojo.generic.GenericResponse;


public class InternationalRechargeFinalResponsePojo extends GenericResponse {

	private List<InternationalRechargeResponsePojo> internationalRechargeDetails;

	public List<InternationalRechargeResponsePojo> getInternationalRechargeDetails() {
		return internationalRechargeDetails;
	}

	public void setInternationalRechargeDetails(List<InternationalRechargeResponsePojo> internationalRechargeDetails) {
		this.internationalRechargeDetails = internationalRechargeDetails;
	}


}
