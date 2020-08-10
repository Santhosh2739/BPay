package ycash.wallet.json.pojo.offers;

public class OfferDetails {

	private String fromDate;
	private String toDate;
	private String startTime;
	private String endTime;
	private Long offerId;
	private String offerName;
	private Integer sNo;
	private String typeofOffer;


	public Integer getsNo() {
		return sNo;
	}

	public void setsNo(Integer sNo) {
		this.sNo = sNo;
	}

	public String getTypeofOffer() {
		return typeofOffer;
	}

	public void setTypeofOffer(String typeofOffer) {
		this.typeofOffer = typeofOffer;
	}

	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Long getOfferId() {
		return offerId;
	}
	public void setOfferId(Long offerId) {
		this.offerId = offerId;
	}
	public String getOfferName() {
		return offerName;
	}
	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}
	
}
