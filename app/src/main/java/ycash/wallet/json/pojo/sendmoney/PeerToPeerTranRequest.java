package ycash.wallet.json.pojo.sendmoney;

import ycash.wallet.json.pojo.generic.GenericRequest;

public class PeerToPeerTranRequest extends GenericRequest{
	
	private String senderMobileNumber;
	private String recipientName;
	private String recipientMobileNumber;
	private double txnAmount;
	private String tpin;
	
	
	public String getSenderMobileNumber() {
		return senderMobileNumber;
	}
	public void setSenderMobileNumber(String senderMobileNumber) {
		this.senderMobileNumber = senderMobileNumber;
	}
	public String getRecipientName() {
		return recipientName;
	}
	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}
	public String getRecipientMobileNumber() {
		return recipientMobileNumber;
	}
	public void setRecipientMobileNumber(String recipientMobileNumber) {
		this.recipientMobileNumber = recipientMobileNumber;
	}
	public double getTxnAmount() {
		return txnAmount;
	}
	public void setTxnAmount(double txnAmount) {
		this.txnAmount = txnAmount;
	}
	public String getTpin() {
		return tpin;
	}
	public void setTpin(String tpin) {
		this.tpin = tpin;
	}
	

}
