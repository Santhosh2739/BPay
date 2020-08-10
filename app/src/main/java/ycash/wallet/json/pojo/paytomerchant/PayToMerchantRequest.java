package ycash.wallet.json.pojo.paytomerchant;

import ycash.wallet.json.pojo.generic.GenericRequest;

/**
 * Created by 10030 on 12/2/2016.
 */
public class PayToMerchantRequest extends GenericRequest {

    private String customerId;
    private double amount;
    private String barcodeData;
    private long clientDate;
    private long barcodeGeneratedDate;

    private String merchantId;

    public String getTpin() {
        return tpin;
    }

    public void setTpin(String tpin) {
        this.tpin = tpin;
    }

    private String tpin;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public long getClientDate() {
        return clientDate;
    }

    public void setClientDate(long clientDate) {
        this.clientDate = clientDate;
    }

    public long getBarcodeGeneratedDate() {
        return barcodeGeneratedDate;
    }

    public void setBarcodeGeneratedDate(long barcodeGeneratedDate) {
        this.barcodeGeneratedDate = barcodeGeneratedDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getBarcodeData() {
        return barcodeData;
    }

    public void setBarcodeData(String barcodeData) {
        this.barcodeData = barcodeData;
    }
}
