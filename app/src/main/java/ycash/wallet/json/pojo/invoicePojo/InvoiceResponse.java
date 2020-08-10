package ycash.wallet.json.pojo.invoicePojo;

import java.math.BigDecimal;
import java.util.Date;

import ycash.wallet.json.pojo.generic.GenericResponse;

/**
 * Created by 10037 on 25-Nov-17.
 */

public class InvoiceResponse extends GenericResponse {
    private String merchantName;
    private String invNo;
    private Date invoiceDate;
    private BigDecimal amount;
    private String mobileNumber;
    private String status;
    private int dueInvoiceCount;
    private BigDecimal balAfter;

    private BigDecimal totalRedemptionPoints;

    public BigDecimal getTotalRedemptionPoints() {
        return totalRedemptionPoints;
    }

    public void setTotalRedemptionPoints(BigDecimal totalRedemptionPoints) {
        this.totalRedemptionPoints = totalRedemptionPoints;
    }


    public BigDecimal getBalAfter() {
        return balAfter;
    }

    public void setBalAfter(BigDecimal balAfter) {
        this.balAfter = balAfter;
    }

    public int getDueInvoiceCount() {
        return dueInvoiceCount;
    }

    public void setDueInvoiceCount(int dueInvoiceCount) {
        this.dueInvoiceCount = dueInvoiceCount;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getInvNo() {
        return invNo;
    }

    public void setInvNo(String invNo) {
        this.invNo = invNo;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
