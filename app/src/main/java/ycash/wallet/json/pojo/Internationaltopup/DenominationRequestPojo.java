package ycash.wallet.json.pojo.Internationaltopup;


import ycash.wallet.json.pojo.generic.GenericRequest;

public class DenominationRequestPojo extends GenericRequest {
	
private String mobileNumber;
private String countryCode;
private String countryName;

public String getMobileNumber() {
	return mobileNumber;
}
public void setMobileNumber(String mobileNumber) {
	this.mobileNumber = mobileNumber;
}
public String getCountryCode() {
	return countryCode;
}
public void setCountryCode(String countryCode) {
	this.countryCode = countryCode;
}
public String getCountryName() {
	return countryName;
}
public void setCountryName(String countryName) {
	this.countryName = countryName;
}

}
