package ycash.wallet.json.pojo.wheretopay;


import ycash.wallet.json.pojo.login.GenericRequest;

public class MerchnatListRequest extends GenericRequest {

    private String categoryID;
    private String merchantName;

	public String getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}
