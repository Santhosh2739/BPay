package ycash.wallet.json.pojo.mobilebilloperator;

import ycash.wallet.json.pojo.generic.GenericRequest;

/**
 * Created by 10037 on 6/19/2017.
 */
public class DomesticRecharge extends GenericRequest {
    private String operatorName;
    private Boolean topup;
    private Boolean billpayment;
    private String denomCollection;
    private Boolean card;
    private String operatorType;
    private Long version;
    private String imagename;

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Boolean getTopup() {
        return topup;
    }

    public void setTopup(Boolean topup) {
        this.topup = topup;
    }

    public Boolean getBillpayment() {
        return billpayment;
    }

    public void setBillpayment(Boolean billpayment) {
        this.billpayment = billpayment;
    }

    public String getDenomCollection() {
        return denomCollection;
    }

    public void setDenomCollection(String denomCollection) {
        this.denomCollection = denomCollection;
    }

    public Boolean getCard() {
        return card;
    }

    public void setCard(Boolean card) {
        this.card = card;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }
}