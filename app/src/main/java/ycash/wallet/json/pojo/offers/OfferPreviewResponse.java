package ycash.wallet.json.pojo.offers;


import ycash.wallet.json.pojo.generic.GenericResponse;

public class OfferPreviewResponse extends GenericResponse {
	
	private String logo;
	private String merchantName;
	private String fromDate;
	private String toDate;
	private String startTime;
	private String endTime;
    private Long offerId;
	private String typeofOffer;

	public String getTypeofOffer() {
		return typeofOffer;
	}

	public void setTypeofOffer(String typeofOffer) {
		this.typeofOffer = typeofOffer;
	}

	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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
	public String getEndIime() {
		return endTime;
	}
	public void setEndIime(String endIime) {
		this.endTime = endIime;
	}

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    @Override
	public String toString() {
		return "OfferPreviewResponse [logo=" + logo + ", merchantName=" + merchantName + ", fromDate=" + fromDate
				+ ", toDate=" + toDate + ", startTime=" + startTime + ", endIime=" + endTime + "]";
	}

	public OfferPreviewResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
}