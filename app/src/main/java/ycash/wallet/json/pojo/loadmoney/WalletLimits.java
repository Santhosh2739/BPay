package ycash.wallet.json.pojo.loadmoney;

import java.util.Date;

import ycash.wallet.json.pojo.generic.GenericResponse;

public class WalletLimits extends GenericResponse {
	
	
    private Long customerID;
    private String walletCustomerID;
    private Byte accountStatus;
    private String mobileNumber;
    private Byte walletStatus;
    private Integer tPIN;
    private Date date;
    private Double availableBalanceCredit;
    private Double outstandingBalance;
    private Double loadmoneyMinPerTxn;
    private Double loadmoneyMaxPerTxn;
    private Double loadmoneyAccpetedDailyTxnVal;
    private Integer loadmoneyDailyTxnCountAvailable;
    
	public Long getCustomerID() {
		return customerID;
	}
	public void setCustomerID(Long customerID) {
		this.customerID = customerID;
	}
	public String getWalletCustomerID() {
		return walletCustomerID;
	}
	public void setWalletCustomerID(String walletCustomerID) {
		this.walletCustomerID = walletCustomerID;
	}
	public Byte getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(Byte accountStatus) {
		this.accountStatus = accountStatus;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public Byte getWalletStatus() {
		return walletStatus;
	}
	public void setWalletStatus(Byte walletStatus) {
		this.walletStatus = walletStatus;
	}
	public Integer gettPIN() {
		return tPIN;
	}
	public void settPIN(Integer tPIN) {
		this.tPIN = tPIN;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Double getAvailableBalanceCredit() {
		return availableBalanceCredit;
	}
	public void setAvailableBalanceCredit(Double availableBalanceCredit) {
		this.availableBalanceCredit = availableBalanceCredit;
	}
	public Double getOutstandingBalance() {
		return outstandingBalance;
	}
	public void setOutstandingBalance(Double outstandingBalance) {
		this.outstandingBalance = outstandingBalance;
	}
	public Double getLoadmoneyMinPerTxn() {
		return loadmoneyMinPerTxn;
	}
	public void setLoadmoneyMinPerTxn(Double loadmoneyMinPerTxn) {
		this.loadmoneyMinPerTxn = loadmoneyMinPerTxn;
	}
	public Double getLoadmoneyMaxPerTxn() {
		return loadmoneyMaxPerTxn;
	}
	public void setLoadmoneyMaxPerTxn(Double loadmoneyMaxPerTxn) {
		this.loadmoneyMaxPerTxn = loadmoneyMaxPerTxn;
	}
	public Double getLoadmoneyAccpetedDailyTxnVal() {
		return loadmoneyAccpetedDailyTxnVal;
	}
	public void setLoadmoneyAccpetedDailyTxnVal(Double loadmoneyAccpetedDailyTxnVal) {
		this.loadmoneyAccpetedDailyTxnVal = loadmoneyAccpetedDailyTxnVal;
	}
	public Integer getLoadmoneyDailyTxnCountAvailable() {
		return loadmoneyDailyTxnCountAvailable;
	}
	public void setLoadmoneyDailyTxnCountAvailable(Integer loadmoneyDailyTxnCountAvailable) {
		this.loadmoneyDailyTxnCountAvailable = loadmoneyDailyTxnCountAvailable;
	}
    
    

}
