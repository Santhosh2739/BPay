package ycash.wallet.json.pojo.userinfo;


import java.math.BigDecimal;
import java.util.ArrayList;

import ycash.wallet.json.pojo.generic.GenericResponse;

/**
 * @author poongodi
 */
public class UserInfoResponse extends GenericResponse {

    public UserInfoResponse() {
        super();
    }

    private String firstName;
    private String lastName;
    private double balance;
    private BigDecimal bpoints;
    private int dueInvoiceCount;
    private ArrayList<String> bannerDetails;

    public ArrayList<String> getBannerDetails() {
        return bannerDetails;
    }

    public void setBannerDetails(ArrayList<String> bannerDetails) {
        this.bannerDetails = bannerDetails;
    }

    public int getDueInvoiceCount() {
        return dueInvoiceCount;
    }

    public void setDueInvoiceCount(int dueInvoiceCount) {
        this.dueInvoiceCount = dueInvoiceCount;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public BigDecimal getBpoints() {
        return bpoints;
    }

    public void setBpoints(BigDecimal bpoints) {
        this.bpoints = bpoints;
    }


}
