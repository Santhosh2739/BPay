package ycash.wallet.json.pojo.sendmoney;


import ycash.wallet.json.pojo.generic.GenericResponse;

public class PeerToPeerTranResponse extends GenericResponse {

	public PeerToPeerTranResponse(String mobileNumber, String status, String txnId, String amt, String customerID,
			String walletBalance,long serverTime) {
		super();
		this.mobileNumber = mobileNumber;
		this.status = status;
		this.txnId = txnId;
		this.amt = amt;
		this.customerID = customerID;
		this.walletBalance = walletBalance;
		this.serverTime=serverTime;
		
	}
	private String mobileNumber;
	private String status;
	private String txnId;
	private String amt;
	private String customerID;
	private String walletBalance;
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
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
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
	public long getServerTime() {
		return serverTime;
	}
	public void setServerTime(long serverTime) {
		this.serverTime = serverTime;
	}
	
	
}
