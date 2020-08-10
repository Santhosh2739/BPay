package ycash.wallet.json.pojo.mobilebilloperator;

import java.util.List;

import ycash.wallet.json.pojo.generic.GenericResponse;

/**
 * Created by 10037 on 6/19/2017.
 */

public class DomesticRechargeResponse extends GenericResponse {
    private List<DomesticRecharge> operatorList;

    public List<DomesticRecharge> getOperatorList() {
        return operatorList;
    }

    public void setOperatorList(List<DomesticRecharge> operatorList) {
        this.operatorList = operatorList;
    }

}
