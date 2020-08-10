package ycash.wallet.json.pojo.wheretopay;

import java.util.List;

public class MerchantCategoryDetailsListPojo {

	private String merchantName;
	private String category;
	private List<BranchDetailsPojo> branch;
	private String path;
	private String merchantAddress;
	private String mobileNumber;

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public List<BranchDetailsPojo> getBranch() {
		return branch;
	}
	public void setBranch(List<BranchDetailsPojo> branch) {
		this.branch = branch;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getMerchantAddress() {
		return merchantAddress;
	}
	public void setMerchantAddress(String merchantAddress) {
		this.merchantAddress = merchantAddress;
	}
	
	
}
