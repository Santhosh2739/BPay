package ycash.wallet.json.pojo.paytomerchant;


import ycash.wallet.json.pojo.login.GenericRequest;

public class P2MBarcodeGenInitValidationRequest extends GenericRequest {
	
	public P2MBarcodeGenInitValidationRequest(){
	}
	private String barcodeData;
	private long barcodeGeneratedDate;
	private double txnAmount;
	private String tpin;
	
	public String getBarcodeData() {
		return barcodeData;
	}
	public void setBarcodeData(String barcodeData) {
		this.barcodeData = barcodeData;
	}
	public long getBarcodeGeneratedDate() {
		return barcodeGeneratedDate;
	}
	public void setBarcodeGeneratedDate(long barcodeGeneratedDate) {
		this.barcodeGeneratedDate = barcodeGeneratedDate;
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
