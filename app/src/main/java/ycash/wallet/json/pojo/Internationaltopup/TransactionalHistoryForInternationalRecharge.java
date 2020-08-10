package ycash.wallet.json.pojo.Internationaltopup;


import java.math.BigDecimal;

import ycash.wallet.json.pojo.generic.GenericResponse;

public class TransactionalHistoryForInternationalRecharge extends GenericResponse {
	private String transactionId;
	private long serverTime;
	private String providerName;
	private String recipientMobileNumber;
	private String denominationAmt;
	private double denominationinKWD;
	private String localCurrency;
	private double senderbalanceAfeter;
	private String tranType;


	private BigDecimal totalRedemptionPoints;

	public BigDecimal getTotalRedemptionPoints() {
		return totalRedemptionPoints;
	}

	public void setTotalRedemptionPoints(BigDecimal totalRedemptionPoints) {
		this.totalRedemptionPoints = totalRedemptionPoints;
	}


	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	public double getSenderbalanceAfeter() {
		return senderbalanceAfeter;
	}

	public void setSenderbalanceAfeter(double senderbalanceAfeter) {
		this.senderbalanceAfeter = senderbalanceAfeter;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public long getServerTime() {
		return serverTime;
	}

	public void setServerTime(long serverTime) {
		this.serverTime = serverTime;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getRecipientMobileNumber() {
		return recipientMobileNumber;
	}

	public void setRecipientMobileNumber(String recipientMobileNumber) {
		this.recipientMobileNumber = recipientMobileNumber;
	}

	public String getDenominationAmt() {
		return denominationAmt;
	}

	public void setDenominationAmt(String denominationAmt) {
		this.denominationAmt = denominationAmt;
	}

	public double getDenominationinKWD() {
		return denominationinKWD;
	}

	public void setDenominationinKWD(double denominationinKWD) {
		this.denominationinKWD = denominationinKWD;
	}

	public String getLocalCurrency() {
		return localCurrency;
	}

	public void setLocalCurrency(String localCurrency) {
		this.localCurrency = localCurrency;
	}
}
