package ycash.wallet.json.pojo.sendmoney;

import ycash.wallet.json.pojo.generic.GenericRequest;

public class PeertoPeerCommitRequest extends GenericRequest {

	private String txnId;
	private String mobileNumber;
	private String txnamt;
	private String walletBalance;
	private String processingFee;
	private String amountToDebit;
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getSenderNumber() {
		return mobileNumber;
	}
	public void setSenderNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getTxnamt() {
		return txnamt;
	}
	public void setTxnamt(String txnamt) {
		this.txnamt = txnamt;
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
	
	}
