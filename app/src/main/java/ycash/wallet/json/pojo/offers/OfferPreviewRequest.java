package ycash.wallet.json.pojo.offers;


import ycash.wallet.json.pojo.generic.GenericRequest;

public class OfferPreviewRequest extends GenericRequest {
	
	private Long offerId;
	private String offerName;
    private Integer useroffertype;
	private String merchantId;
	private String merchantName;

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Integer getUseroffertype() {
        return useroffertype;
    }

    public void setUseroffertype(Integer useroffertype) {
        this.useroffertype = useroffertype;
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
