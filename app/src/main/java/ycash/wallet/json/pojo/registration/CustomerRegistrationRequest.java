package ycash.wallet.json.pojo.registration;

import java.io.Serializable;
import java.sql.Blob;

import ycash.wallet.json.pojo.login.GenericRequest;


/**
 * 
 * @author poongodi
 *
 */

public class CustomerRegistrationRequest extends GenericRequest {

	private static final long serialVersionUID = -6351977117167548295L;
	private String agentId;
	private String originatingCountry;
	private String civilID;
	private String customer_Civil_Id_Expiry_Date;
	private String firstName;
	private String lastName;
	private String mobileNumber;
	private String emailID;
	private Blob customerCivilIDCopy;
	private String language;
	private String msisdn;
	private String deviceIdNumber;
	private String Civil_ID_Image_Front;
	private String Civil_ID_Image_Back;
	private Blob civilIDImageFront;
	private Blob civilIDImageBack;
	private String pin;
	private String password;
	private String confirmPassword;
	//private String walletUser;
//	public String getWalletUser() {
//		return walletUser;
//	}
//
//	public void setWalletUser(String walletUser) {
//		this.walletUser = walletUser;
//	}

	private String nationality;
	private String gender;
    private String walletTypeDesc;
	//private String walletUser;



	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getWalletTypeDesc() {
        return walletTypeDesc;
    }

    public void setWalletTypeDesc(String walletTypeDesc) {
        this.walletTypeDesc = walletTypeDesc;
    }
	public Blob getCivilIDImageFront() {
		return civilIDImageFront;
	}
	public void setCivilIDImageFront(Blob civilIDImageFront) {
		this.civilIDImageFront = civilIDImageFront;
	}
	public Blob getCivilIDImageBack() {
		return civilIDImageBack;
	}

	public void setCivilIDImageBack(Blob civilIDImageBack) {
		this.civilIDImageBack = civilIDImageBack;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getDeviceIdNumber() {
		return deviceIdNumber;
	}
	public void setDeviceIdNumber(String deviceIdNumber) {
		this.deviceIdNumber = deviceIdNumber;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getOriginatingCountry() {
		return originatingCountry;
	}

	public void setOriginatingCountry(String originatingCountry) {
		this.originatingCountry = originatingCountry;
	}

	public String getCivilID() {
		return civilID;
	}

	public void setCivilID(String civilID) {
		this.civilID = civilID;
	}

	public String getCustomer_Civil_Id_Expiry_Date() {
		return customer_Civil_Id_Expiry_Date;
	}

	public void setCustomer_Civil_Id_Expiry_Date(String customer_Civil_Id_Expiry_Date) {
		this.customer_Civil_Id_Expiry_Date = customer_Civil_Id_Expiry_Date;
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

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public Blob getCustomerCivilIDCopy() {
		return customerCivilIDCopy;
	}

	public void setCustomerCivilIDCopy(Blob customerCivilIDCopy) {
		this.customerCivilIDCopy = customerCivilIDCopy;
	}

	public String getCivil_ID_Image_Front() {
		return Civil_ID_Image_Front;
	}

	public void setCivil_ID_Image_Front(String civil_ID_Image_Front) {
		Civil_ID_Image_Front = civil_ID_Image_Front;
	}

	public String getCivil_ID_Image_Back() {
		return Civil_ID_Image_Back;
	}

	public void setCivil_ID_Image_Back(String civil_ID_Image_Back) {
		Civil_ID_Image_Back = civil_ID_Image_Back;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Override
	public String toString() {
		return "CustomerRegistrationRequest{" +
				"agentId='" + agentId + '\'' +
				", originatingCountry='" + originatingCountry + '\'' +
				", civilID='" + civilID + '\'' +
				", customer_Civil_Id_Expiry_Date='" + customer_Civil_Id_Expiry_Date + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", mobileNumber='" + mobileNumber + '\'' +
				", emailID='" + emailID + '\'' +
				", customerCivilIDCopy=" + customerCivilIDCopy +
				", language='" + language + '\'' +
				", msisdn='" + msisdn + '\'' +
				", deviceIdNumber='" + deviceIdNumber + '\'' +
				", civilIDImageFront=" + civilIDImageFront +
				", civilIDImageBack=" + civilIDImageBack +
				", pin='" + pin + '\'' +
				", password='" + password + '\'' +
				", confirmPassword='" + confirmPassword + '\'' +
				", nationality='" + nationality + '\'' +
				", gender='" + gender + '\'' +
				", walletTypeDesc='" + walletTypeDesc + '\'' +
				'}';
	}
}
