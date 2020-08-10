package ycash.wallet.json.pojo.userinfo;


import ycash.wallet.json.pojo.generic.GenericResponse;

/**
 * 
 * @author poongodi
 *
 */
public class MerchantInfoResponse extends GenericResponse {
	

	
	private String merchantName;
	private String mobileNumber;
	private double merchant_balance;
	
	
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public double getMerchant_balance() {
		return merchant_balance;
	}
	public void setMerchant_balance(double merchant_balance) {
		this.merchant_balance = merchant_balance;
	}
	
}
