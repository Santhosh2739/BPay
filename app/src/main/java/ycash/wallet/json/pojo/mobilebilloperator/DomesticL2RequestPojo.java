package ycash.wallet.json.pojo.mobilebilloperator;

import java.util.List;

import ycash.wallet.json.pojo.generic.GenericRequest;

/**
 * Created by 10037 on 6/20/2017.
 */

public class DomesticL2RequestPojo extends GenericRequest {

    private String txnAmount;
    private String txnRefNo;

    private List<String> txnList;
    private Integer quantity;

    public List<String> getTxnList() {
        return txnList;
    }

    public void setTxnList(List<String> txnList) {
        this.txnList = txnList;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getTxnAmount() {
        return txnAmount;
    }

    public void setTxnAmount(String txnAmount) {
        this.txnAmount = txnAmount;
    }

    public String getTxnRefNo() {
        return txnRefNo;
    }

    public void setTxnRefNo(String txnRefNo) {
        this.txnRefNo = txnRefNo;
    }
}
