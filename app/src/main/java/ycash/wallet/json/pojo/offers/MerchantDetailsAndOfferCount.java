package ycash.wallet.json.pojo.offers;

public class MerchantDetailsAndOfferCount {
private String merchantName;
private String offerCount;
private String merchantId;
private String merchantLogo;


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
public String getOfferCount() {
	return offerCount;
}
public void setOfferCount(String offerCount) {
	this.offerCount = offerCount;
}
public String getMerchantLogo() {
	return merchantLogo;
}
public void setMerchantLogo(String merchantLogo) {
	this.merchantLogo = merchantLogo;
}

}
