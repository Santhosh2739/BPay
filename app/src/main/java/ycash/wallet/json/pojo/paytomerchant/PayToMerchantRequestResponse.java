package ycash.wallet.json.pojo.paytomerchant;


import java.math.BigDecimal;

import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.generic.TransType;

public class PayToMerchantRequestResponse extends GenericResponse {

	private String transactionId;
	private double balance;
	private double processingfee;
	private double total;
	private long serverTime;
	private String customerId;



	private double txnAmount;
	private String recipientName;
	private String recipientMobileNumber;
	private String customerMobileNumber;


	
	/*public PayToMerchantRequestResponse(int g_status, String g_status_description,
			String g_errorDescription, String g_response_trans_type, String transactionId,
			double balance, double processingfee, double total,
			long serverTime, String customerId, double txnAmount, String recipientName, 
			String recipientMobileNumber, String customerMobileNumber) {
		super(g_status, g_status_description, g_errorDescription, g_response_trans_type);
		this.transactionId = transactionId;
		this.balance = balance;
		this.processingfee = processingfee;
		this.total = total;
		this.serverTime = serverTime;
		this.customerId = customerId;
		this.txnAmount = txnAmount;
		this.recipientName = recipientName;
		this.recipientMobileNumber = recipientMobileNumber;
		this.customerMobileNumber = customerMobileNumber;
	}
	public PayToMerchantRequestResponse(int g_status, String g_status_description,
			String g_errorDescription, String g_response_trans_type, String transactionId,
			double balance, double processingfee, double total,
			long serverTime, String customerId, double txnAmount) {
		super(g_status, g_status_description, g_errorDescription, g_response_trans_type);
		this.transactionId = transactionId;
		this.balance = balance;
		this.processingfee = processingfee;
		this.total = total;
		this.serverTime = serverTime;
		this.customerId = customerId;
		this.txnAmount = txnAmount;
	}
	*/
	public String getCustomerMobileNumber() {
		return customerMobileNumber;
	}
	public void setCustomerMobileNumber(String customerMobileNumber) {
		this.customerMobileNumber = customerMobileNumber;
	}
	public PayToMerchantRequestResponse(){
		setG_response_trans_type(TransType.PAY_TO_MERCHANT_RESPONSE.name());
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
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}	
}
