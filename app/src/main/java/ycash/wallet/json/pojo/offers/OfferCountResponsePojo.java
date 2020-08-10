package ycash.wallet.json.pojo.offers;

import java.util.List;

public class OfferCountResponsePojo {

	private List<MerchantDetailsAndOfferCount> newOffers;
	private List<MerchantDetailsAndOfferCount> myactiveOffers;
	private String totalNewOffers;
	private String totalActiveoffers;
	
	public List<MerchantDetailsAndOfferCount> getNewOffers() {
		return newOffers;
	}
	public void setNewOffers(List<MerchantDetailsAndOfferCount> newOffers) {
		this.newOffers = newOffers;
	}
	public List<MerchantDetailsAndOfferCount> getMyactiveOffers() {
		return myactiveOffers;
	}
	public void setMyactiveOffers(List<MerchantDetailsAndOfferCount> myactiveOffers) {
		this.myactiveOffers = myactiveOffers;
	}
	public String getTotalNewOffers() {
		return totalNewOffers;
	}
	public void setTotalNewOffers(String totalNewOffers) {
		this.totalNewOffers = totalNewOffers;
	}
	public String getTotalActiveoffers() {
		return totalActiveoffers;
	}
	public void setTotalActiveoffers(String totalActiveoffers) {
		this.totalActiveoffers = totalActiveoffers;
	}
	
	
}
