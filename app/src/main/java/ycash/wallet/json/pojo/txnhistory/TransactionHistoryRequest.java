package ycash.wallet.json.pojo.txnhistory;


import ycash.wallet.json.pojo.generic.GenericRequest;

public class TransactionHistoryRequest extends GenericRequest {


    public String getDeviceIdNumber() {
        return deviceIdNumber;
    }

    public void setDeviceIdNumber(String deviceIdNumber) {
        this.deviceIdNumber = deviceIdNumber;
    }

    String deviceIdNumber;

    private int from;
    //	private int count;
    private String tranType;

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }
//	public int getCount() {
//		return count;
//	}
//	public void setCount(int count) {
//		this.count = count;
//	}

}
