package ycash.wallet.json.pojo.offers;

public class Offers {
	
	private String offerName;
	private String fromDate;
	private String toDate;
	private String startTime;
    private Long offerId;
	private String endIime;
    private String merchantLogo;
    private String merchantName;
    private String offerType;
	public String getOfferName() {
		return offerName;
	}
	public void setOfferName(String offerName) {
		this.offerName = offerName;
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
		return endIime;
	}
	public void setEndIime(String endIime) {
		this.endIime = endIime;
	}
    public Long getOfferId() {
        return offerId;
    }
    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public String getMerchantLogo() {
        return merchantLogo;
    }

    public void setMerchantLogo(String merchantLogo) {
        this.merchantLogo = merchantLogo;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    @Override
	public String toString() {
		return "Offers [offerName=" + offerName + ", fromDate=" + fromDate + ", toDate=" + toDate + ", startTime="
				+ startTime + ", endIime=" + endIime + "]";
	}
	public Offers(String offerName, String fromDate, String toDate, String startTime, String endIime) {
		super();
		this.offerName = offerName;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.startTime = startTime;
		this.endIime = endIime;
	}
	public Offers() {
		super();
	}
}
