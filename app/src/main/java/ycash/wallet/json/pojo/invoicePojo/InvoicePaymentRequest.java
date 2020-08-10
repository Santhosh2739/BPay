package ycash.wallet.json.pojo.invoicePojo;

import ycash.wallet.json.pojo.generic.GenericRequest;

/**
 * Created by 10037 on 24-Nov-17.
 */

public class InvoicePaymentRequest extends GenericRequest {
    private String invNo;
    private Double amount;
    private int status;
    private String reason;
    private String tpin;
    private String merchantName;
    private Long offerId;

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getTpin() {
        return tpin;
    }

    public void setTpin(String tpin) {
        this.tpin = tpin;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInvNo() {
        return invNo;
    }

    public void setInvNo(String invNo) {
        this.invNo = invNo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
