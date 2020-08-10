package ycash.wallet.json.pojo.incomingmoney;


import ycash.wallet.json.pojo.generic.GenericResponse;

/**
 * 
 * @author mohit
 *
 */
public class SendMoneyIncomingResponse extends GenericResponse{

	private String transactionId;
	private double txnAmount;
	private double processingfee;
	private double total;
	private String tranType;

	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	private double balanceBefore;
	private double balanceAfter;
	private long serverTime;
	private String senderMobileNumber;
	private String senderName;
	
	public double getTxnAmount() {
		return txnAmount;
	}
	public void setTxnAmount(double txnAmount) {
		this.txnAmount = txnAmount;
	}
	public double getBalanceBefore() {
		return balanceBefore;
	}
	public void setBalanceBefore(double balanceBefore) {
		this.balanceBefore = balanceBefore;
	}
	public double getBalanceAfter() {
		return balanceAfter;
	}
	public void setBalanceAfter(double balanceAfter) {
		this.balanceAfter = balanceAfter;
	}
	public String getSenderMobileNumber() {
		return senderMobileNumber;
	}
	public void setSenderMobileNumber(String senderMobileNumber) {
		this.senderMobileNumber = senderMobileNumber;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public double getProcessingfee() {
		return processingfee;
	}
	public void setProcessingfee(double processingfee) {
		this.processingfee = processingfee;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public long getServerTime() {
		return serverTime;
	}
	public void setServerTime(long serverTime) {
		this.serverTime = serverTime;
	}
}
