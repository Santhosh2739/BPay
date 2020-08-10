package ycash.wallet.json.pojo.offers;


import ycash.wallet.json.pojo.generic.GenericResponse;

public class OfferFinalResponse extends GenericResponse {



	private OfferCountResponsePojo offerCount;
	
	private OfferDetailsMerchantWise offerDetails;
	

	public OfferDetailsMerchantWise getOfferDetails() {
		return offerDetails;
	}

	public void setOfferDetails(OfferDetailsMerchantWise offerDetails) {
		this.offerDetails = offerDetails;
	}

	public OfferCountResponsePojo getOfferCount() {
		return offerCount;
	}

	public void setOfferCount(OfferCountResponsePojo offerCount) {
		this.offerCount = offerCount;
	}
	
	
}
