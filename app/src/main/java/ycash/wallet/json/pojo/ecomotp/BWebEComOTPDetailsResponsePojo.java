package ycash.wallet.json.pojo.ecomotp;

import java.util.ArrayList;

import ycash.wallet.json.pojo.generic.GenericResponse;
import ycash.wallet.json.pojo.invoicePojo.InvoiceDetailsPojo;

public class BWebEComOTPDetailsResponsePojo extends GenericResponse {

    public ArrayList<BWebEComOTP> getOtpList() {
        return otpList;
    }

    public void setOtpList(ArrayList<BWebEComOTP> otpList) {
        this.otpList = otpList;
    }

    ArrayList<BWebEComOTP> otpList;


}
