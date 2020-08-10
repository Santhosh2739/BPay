package ycash.wallet.json.pojo.invoicePojo;

import ycash.wallet.json.pojo.generic.GenericRequest;

/**
 * Created by 10037 on 22-Nov-17.
 */

public class InvoiceDetailsPojo extends GenericRequest {
    private String merchantName;
    private String invNo;
    private String invoiceDate;
    private double amount;
    private boolean isMerchantRequest;
    private String description;
    private String customerName;
    private String customerEmailId;

    public byte[] getArabicCustomerName() {
        return arabicCustomerName;
    }

    public void setArabicCustomerName(byte[] arabicCustomerName) {
        this.arabicCustomerName = arabicCustomerName;
    }

    public byte[] getArabicDescription() {
        return arabicDescription;
    }

    public void setArabicDescription(byte[] arabicDescription) {
        this.arabicDescription = arabicDescription;
    }

    private byte[] arabicCustomerName;
    private byte[] arabicDescription;


    //offer
    private Long offerId;
    private double discount;
    private double discountPrice;
    private double totalAmt;

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    private String offerDescription;

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmailId() {
        return customerEmailId;
    }

    public void setCustomerEmailId(String customerEmailId) {
        this.customerEmailId = customerEmailId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isMerchantRequest() {
        return isMerchantRequest;
    }

    public void setMerchantRequest(boolean merchantRequest) {
        isMerchantRequest = merchantRequest;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public InvoiceDetailsPojo(String invNo, String invoiceDate, String merchantName, double amount,boolean isMerchantRequest,String description,String customerName,String customerEmailId) {
        this.merchantName = merchantName;
        this.invNo = invNo;
        this.invoiceDate = invoiceDate;
        this.amount = amount;
        this.isMerchantRequest=isMerchantRequest;
        this.description=description;
        this.customerName=customerName;
        this.customerEmailId=customerEmailId;

    }

    /*public InvoiceDetailsPojo(String invNo, String invoiceDate) {
        this.invNo = invNo;
        this.invoiceDate = invoiceDate;
    }*/


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

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }


}
