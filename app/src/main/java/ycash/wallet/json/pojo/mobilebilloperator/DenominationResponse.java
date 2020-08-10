package ycash.wallet.json.pojo.mobilebilloperator;

import java.util.List;

import ycash.wallet.json.pojo.generic.GenericResponse;

/**
 * Created by 10037 on 7/18/2017.
 */

public class DenominationResponse extends GenericResponse {
    private String operatorName;
    private String operatorType;
    private List<String> denom;
    private List<String> denomWithKD;

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public List<String> getDenom() {
        return denom;
    }

    public void setDenom(List<String> denom) {
        this.denom = denom;
    }

    public List<String> getDenomWithKD() {
        return denomWithKD;
    }

    public void setDenomWithKD(List<String> denomWithKD) {
        this.denomWithKD = denomWithKD;
    }
}
