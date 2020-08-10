package ycash.wallet.json.pojo.loadmoney;

import java.io.Serializable;
import java.util.Date;


public class PaymentVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8585684094638115745L;
	
	

	private String transactionRefNo;
	private String authIdCode;
	private String result;	
	private String paymentId;	
	private String tranId;
	private String postDate;
	private String trackid;
	public String getTransactionRefNo() {
		return transactionRefNo;
	}
	public void setTransactionRefNo(String transactionRefNo) {
		this.transactionRefNo = transactionRefNo;
	}
	public String getAuthIdCode() {
		return authIdCode;
	}
	public void setAuthIdCode(String authIdCode) {
		this.authIdCode = authIdCode;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getTranId() {
		return tranId;
	}
	public void setTranId(String tranId) {
		this.tranId = tranId;
	}
	public String getPostDate() {
		return postDate;
	}
	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}
	public String getTrackid() {
		return trackid;
	}
	public void setTrackid(String trackid) {
		this.trackid = trackid;
	}
	@Override
	public String toString() {
		return "PaymentVO [transactionRefNo=" + transactionRefNo + ", authIdCode=" + authIdCode + ", result=" + result
				+ ", paymentId=" + paymentId + ", tranId=" + tranId + ", postDate=" + postDate + ", trackid=" + trackid
				+ "]";
	}
}
