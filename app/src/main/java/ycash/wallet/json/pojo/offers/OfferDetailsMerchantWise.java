package ycash.wallet.json.pojo.offers;

import java.util.List;

public class OfferDetailsMerchantWise {

	private String merchantName;
	private String merchantID;
	private String merchantlogo;
	private List<OfferDetails> offerDetails ;
	

	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getMerchantID() {
		return merchantID;
	}
	public void setMerchantID(String merchantID) {
		this.merchantID = merchantID;
	}
	public String getMerchantlogo() {
		return merchantlogo;
	}
	public void setMerchantlogo(String merchantlogo) {
		this.merchantlogo = merchantlogo;
	}
	public List<OfferDetails> getOfferDetails() {
		return offerDetails;
	}
	public void setOfferDetails(List<OfferDetails> offerDetails) {
		this.offerDetails = offerDetails;
	}
	
}
