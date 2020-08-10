package ycash.wallet.json.pojo.login;


import ycash.wallet.json.pojo.generic.TransType;

public class CustomerLoginRequest extends GenericRequest{
	
	private String login_pin;
    private String mobileNumber;
    private String deviceIdNumber;
    private String ipAddress;
    
    private int deviceType;
	private String lat;  
	private String lon;
	private String language;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getGcmRegistrationId() {
		return gcmRegistrationId;
	}

	public void setGcmRegistrationId(String gcmRegistrationId) {
		this.gcmRegistrationId = gcmRegistrationId;
	}

	private String gcmRegistrationId;
	private String walletApplicationVersion; 
	private String deviceOsVersionDetails1;  
	private String deviceOsVersionDetails2; 
	
	
	public int getDeviceType() {
		return deviceType;
	}



	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}



	public String getLat() {
		return lat;
	}



	public void setLat(String lat) {
		this.lat = lat;
	}



	public String getLon() {
		return lon;
	}



	public void setLon(String lon) {
		this.lon = lon;
	}






	public String getWalletApplicationVersion() {
		return walletApplicationVersion;
	}



	public void setWalletApplicationVersion(String walletApplicationVersion) {
		this.walletApplicationVersion = walletApplicationVersion;
	}



	public String getDeviceOsVersionDetails1() {
		return deviceOsVersionDetails1;
	}



	public void setDeviceOsVersionDetails1(String deviceOsVersionDetails1) {
		this.deviceOsVersionDetails1 = deviceOsVersionDetails1;
	}



	public String getDeviceOsVersionDetails2() {
		return deviceOsVersionDetails2;
	}



	public void setDeviceOsVersionDetails2(String deviceOsVersionDetails2) {
		this.deviceOsVersionDetails2 = deviceOsVersionDetails2;
	}



	public String getIpAddress() {
		return ipAddress;
	}



	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}



	public String getLogin_pin() {
		return login_pin;
	}



	public void setLogin_pin(String login_pin) {
		this.login_pin = login_pin;
	}



	public String getMobileNumber() {
		return mobileNumber;
	}



	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}



	public String getDeviceIdNumber() {
		return deviceIdNumber;
	}



	public void setDeviceIdNumber(String deviceIdNumber) {
		this.deviceIdNumber = deviceIdNumber;
	}



	public CustomerLoginRequest(){
		super.setG_transType(TransType.LOGIN_CUSTOMER.name());
	}

}
