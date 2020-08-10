package ycash.wallet.json.pojo.generic;


public class ViewProfileResponse extends GenericResponse{
	private String mobileNumber;
	private String emailId;
	private String civilId;
	private String firstName;
	private String lastName;
	private String customerId;
	private String walletBalance;
	
	public ViewProfileResponse(String mobileNumber, String emailId, String civilId, String firstName, String lastName,
			String customerId, String walletBalance) {
		super();
		this.mobileNumber = mobileNumber;
		this.emailId = emailId;
		this.civilId = civilId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.customerId = customerId;
		this.walletBalance = walletBalance;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getCivilId() {
		return civilId;
	}
	public void setCivilId(String civilId) {
		this.civilId = civilId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getWalletBalance() {
		return walletBalance;
	}
	public void setWalletBalance(String walletBalance) {
		this.walletBalance = walletBalance;
	}
	@Override
	public String toString() {
		return "ViewProfileResponse [mobileNumber=" + mobileNumber + ", emailId=" + emailId + ", civilId=" + civilId
				+ ", firstName=" + firstName + ", lastName=" + lastName + ", customerId=" + customerId
				+ ", walletBalance=" + walletBalance + "]";
	}
	
	

}
