package ycash.wallet.json.pojo.sendmoney;


import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;

public class PayToMerchantCommitRequestResponse extends GenericResponse {
	
	private String transactionId;
	private double balance;
	private double processingfee;
	private double total;
	private long serverTime;
	private long serverTimeL2;
	private String customerId;
	private double txnAmount;
	

	
	public PayToMerchantCommitRequestResponse(){
		setG_response_trans_type(TransType.PAY_TO_MERCHANT_COMMIT_REQUEST_RESPONSE.name());
	}
	
	public double getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(double txnAmount) {
		this.txnAmount = txnAmount;
	}

	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
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
	public long getServerTimeL2() {
		return serverTimeL2;
	}
	public void setServerTimeL2(long serverTimeL2) {
		this.serverTimeL2 = serverTimeL2;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
}
