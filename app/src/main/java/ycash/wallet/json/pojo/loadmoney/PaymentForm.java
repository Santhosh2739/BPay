package ycash.wallet.json.pojo.loadmoney;

import ycash.wallet.json.pojo.generic.GenericRequest;

public class PaymentForm extends GenericRequest{
	private String product;
	private String total;
	private String qty;
	private String ErrorText;
	private String postdate;
	private String tranid;
	private String auth;
	private String trackid;
	private String ref;
	private String result;
	private String udf1;
	private String udf2;
	private String udf3;
	private String udf4;
	private String udf5;
	private String paymentid;
	private String mobileNumber;
	private String reqType;
	private Double price;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getReqType() {
		return reqType;
	}
	public void setReqType(String reqType) {
		this.reqType = reqType;
	}
	public String getErrorText() {
		return ErrorText;
	}
	public void setErrorText(String errorText) {
		ErrorText = errorText;
	}
	public String getPostdate() {
		return postdate;
	}
	public void setPostdate(String postdate) {
		this.postdate = postdate;
	}
	public String getTranid() {
		return tranid;
	}
	public void setTranid(String tranid) {
		this.tranid = tranid;
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public String getTrackid() {
		return trackid;
	}
	public void setTrackid(String trackid) {
		this.trackid = trackid;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getUdf1() {
		return udf1;
	}
	public void setUdf1(String udf1) {
		this.udf1 = udf1;
	}
	public String getUdf2() {
		return udf2;
	}
	public void setUdf2(String udf2) {
		this.udf2 = udf2;
	}
	public String getUdf3() {
		return udf3;
	}
	public void setUdf3(String udf3) {
		this.udf3 = udf3;
	}
	public String getUdf4() {
		return udf4;
	}
	public void setUdf4(String udf4) {
		this.udf4 = udf4;
	}
	public String getUdf5() {
		return udf5;
	}
	public void setUdf5(String udf5) {
		this.udf5 = udf5;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}

	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	
	public String getPaymentid() {
		return paymentid;
	}
	public void setPaymentid(String paymentid) {
		this.paymentid = paymentid;
	}
	@Override
	public String toString() {
		return "PaymentForm [product=" + product + ", price=" + price + ", total=" + total + ", qty=" + qty
				+ ", ErrorText=" + ErrorText + ", postdate=" + postdate + ", tranid=" + tranid + ", auth=" + auth
				+ ", trackid=" + trackid + ", ref=" + ref + ", result=" + result + ", udf1=" + udf1 + ", udf2=" + udf2
				+ ", udf3=" + udf3 + ", udf4=" + udf4 + ", udf5=" + udf5 + ", paymentId=" + paymentid + "]";
	}
	
	
}
