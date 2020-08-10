package ycash.wallet.json.pojo.sendmoney;


import ycash.wallet.json.pojo.generic.GenericResponse;

public class PeertoPeerConformationResponse extends GenericResponse {

	
	public PeertoPeerConformationResponse(String mobileNumber, String status, String txnId, String txnamt,
			String customerID, String walletBalance, String processingFee, String amountToDebit, long serverTime) {
		super();
		this.mobileNumber = mobileNumber;
		this.status = status;
		this.txnId = txnId;
		this.txnamt = txnamt;
		this.customerID = customerID;
		this.walletBalance = walletBalance;
		this.processingFee = processingFee;
		this.amountToDebit = amountToDebit;
		this.serverTime = serverTime;
	}
	private String mobileNumber;
	private String status;
	private String txnId;
	private String txnamt;
	private String customerID;
	private String walletBalance;
	private String processingFee;
	private String amountToDebit;
	private long serverTime;
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getTxnamt() {
		return txnamt;
	}
	public void setTxnamt(String txnamt) {
		this.txnamt = txnamt;
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public String getWalletBalance() {
		return walletBalance;
	}
	public void setWalletBalance(String walletBalance) {
		this.walletBalance = walletBalance;
	}
	public String getProcessingFee() {
		return processingFee;
	}
	public void setProcessingFee(String processingFee) {
		this.processingFee = processingFee;
	}
	public String getAmountToDebit() {
		return amountToDebit;
	}
	public void setAmountToDebit(String amountToDebit) {
		this.amountToDebit = amountToDebit;
	}
	public long getServerTime() {
		return serverTime;
	}
	public void setServerTime(long serverTime) {
		this.serverTime = serverTime;
	}
	
	
	
}
